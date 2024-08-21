package niap.codeporter

import JavaLexer
import JavaParser
import JavaParserBaseListener
import com.google.common.base.CaseFormat
import com.google.common.base.CaseFormat.LOWER_CAMEL
import com.google.common.base.CaseFormat.UPPER_CAMEL
import com.google.common.base.CaseFormat.UPPER_UNDERSCORE
import org.antlr.v4.runtime.TokenStreamRewriter
import org.antlr.v4.runtime.TokenSource
import org.antlr.v4.runtime.CharStreams
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.ParserRuleContext
import org.antlr.v4.runtime.misc.Interval
import org.antlr.v4.runtime.tree.ParseTreeWalker
import org.gradle.api.DefaultTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.io.InputStream
import java.nio.charset.Charset


fun String.caseFormat(from: CaseFormat, to: CaseFormat): String {
    return from.to(to, this)
}

class CodePorterPlugin:Plugin<Project> {

    override fun apply(project:Project){

        project.configurations.getByName("implementation").setCanBeResolved(true)
        project.configurations.getByName("api").setCanBeResolved(true)

        project.tasks.register("codeporter",CodePorterTask::class.java) { task ->
            task.description = "run code porter"
            task.group = "from code-porter plugin"
            task.classpath = project.files();//configurations.getByName("api").files
            //task.classpath = ccp
            task.doLast {
                println("Hello from plugin 'niap.codeporter'")
            }
        }
    }
}



abstract class CodePorterTask : DefaultTask(){

    @InputFiles
    var classpath: Iterable<File>? = null
    @InputFiles
    val src_main_java =project.project.projectDir.toString()+"/src/main/java"
    @InputFiles
    val gen_out_java =project.project.rootDir.toString()+"/package/gen/java"

    data class TestCaseSource (
        val permissionLabel:String="",
        val permissionLabelCamel:String="",
        val sdkMin:Int=0,
        val sdkMax:Int=100000,
        val isCustom:Boolean=false,
        val impl:String=""
    )

    companion object {
        fun getFullText(context: ParserRuleContext): String {
            if (context.start == null || context.stop == null || context.start.getStartIndex() < 0 || context.stop.getStopIndex() < 0) return context.getText()

            return context.start.getInputStream()
                .getText(Interval.of(context.start.getStartIndex(), context.stop.getStopIndex()))
        }
        var testCases = mutableListOf<TestCaseSource>()
    }

    class AstListener : JavaParserBaseListener() {
        class InnerArgListener(val permission:String) : JavaParserBaseListener() {
            override fun enterCreator(ctx: JavaParser.CreatorContext?) {
                super.enterCreator(ctx)
                var i=0
                val clsExpressions =
                    ctx?.classCreatorRest()?.arguments()?.expressionList()?.expression()
                if(clsExpressions != null && ctx.createdName().text.equals("PermissionTest")) {
                    println("creator context:${permission},${clsExpressions.size}," + ctx!!.createdName().text)
                    val args = mutableListOf<String>()
                    clsExpressions.forEach {
                        val expr: String = getFullText(it)
                        args.add(expr)
                    }
                    val permission_ = permission
                        .replace("\"","")
                        .replace("SignaturePermissions.permission.","")
                        .replace("Manifest.permission.","")
                        .replace("android.permission.","")
                        .replace("permission.","")
                    val camel_ = permission_.caseFormat(UPPER_UNDERSCORE, UPPER_CAMEL)
                    val impl = args[clsExpressions.size-1]
                    val isCustom = if(args[0].equals("true")) true else false
                    fun fromCodeToInt(code:String):Int {
                        var cnum: Int? = code.toIntOrNull()
                        if (cnum != null) {
                            return cnum
                        } else {
                            if (code.endsWith(".UPSIDE_DOWN_CAKE")) {
                                return 34
                            } else if (code.endsWith(".TIRAMISU")) {
                                return 33
                            } else if (code.endsWith(".S_V2")) {
                                return 32
                            } else if(code.endsWith(".S")){
                                return 31
                            } else if(code.endsWith(".R")){
                                return 30
                            } else if(code.endsWith(".Q")){
                                return 29
                            } else if(code.endsWith(".P")) {
                                return 28
                            } else if(code.endsWith(".O")) {
                                return 26
                            } else if(code.endsWith(".N")) {
                                return 24 //MR1 25
                            } else if(code.endsWith(".M")) {
                                //TEMPORARY_ENABLE_ACCESSIBILITY use this?
                                return 23
                            } else {
                                println(code)
                                return 0//Unknown
                            }
                        }
                    }

                    var sdkMin = 0
                    var sdkMax = 100000
                    if(args.size>=3){
                        sdkMin=fromCodeToInt(args[1])
                        if(args.size==4){
                            sdkMax=fromCodeToInt(args[2])
                        }
                    }
                    val testCS = TestCaseSource(
                        permission_,
                        camel_,sdkMin,sdkMax,isCustom,impl
                        )
                    //println(testCS)
                    testCases.add(testCS)
                }
            }
        }

        override fun enterMethodCall(ctx: JavaParser.MethodCallContext?) {
            super.enterMethodCall(ctx)
            if (ctx !== null) {
                if(ctx.identifier()?.text.equals("put")){
                    //println("method call:" + ctx.identifier()?.text)
                    val exprList = ctx.arguments().expressionList()
                    if(exprList != null) {
                        var i=0
                        var permission = ""
                        exprList.expression()?.forEach {
                            val expr:String = getFullText(it)
                            //println("expr($i)=>" + expr)
                            if(i==0) permission = expr
                            if(expr.startsWith("new PermissionTest(")){
                                val expr2 = "class Dummy { public Dummy(){ PermissionTest t = $expr; }}"
                                val lexer = JavaLexer(CharStreams.fromString(expr2))
                                val parser = JavaParser(CommonTokenStream(lexer))
                                val cu = parser.compilationUnit()
                                val walker = ParseTreeWalker()
                                walker.walk(InnerArgListener(permission),cu)
                            }
                            i++
                        }
                    }
                }
            }
        }
    }

    @TaskAction
    fun action(){

        if(!File(gen_out_java).exists()){
            File(gen_out_java).mkdirs()
        }

        File(src_main_java).walk().forEach {
            if (it.extension == "java" &&
                it.nameWithoutExtension.endsWith("PermissionTester")) {
                println(it.nameWithoutExtension)
                val typeSignature = it.nameWithoutExtension.replace("PermissionTester","");
                val ins: InputStream = it.inputStream()
                val content = ins.readBytes().toString(Charset.defaultCharset())

                val lexer = JavaLexer(CharStreams.fromString(content))
                val parser = JavaParser(CommonTokenStream(lexer))
                val walker = ParseTreeWalker()
                walker.walk(AstListener(),parser.compilationUnit())

                if(testCases.size > 0){
                    println("testCases no=${testCases.size}")
                    val clsName = typeSignature+"PermissionTestModule";
                    val fout = File(gen_out_java+"//$clsName.java")
                    //fout.createNewFile()
                    fout.writeText(
"""//Auto generated file ${clsName}.java by CoderPorterPlugin
/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.android.certification.niap.permission.dpctester.test;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;

import androidx.annotation.NonNull;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestModuleBase;
import com.android.certification.niap.permission.dpctester.test.runner.PermissionTestRunner;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTest;
import com.android.certification.niap.permission.dpctester.test.tool.PermissionTestModule;
import com.android.certification.niap.permission.dpctester.test.exception.BypassTestException;
import com.android.certification.niap.permission.dpctester.test.exception.UnexpectedTesFailureException;
import static android.Manifest.permission.*;
import java.util.function.Consumer;
     
@PermissionTestModule(name="${typeSignature} Test Cases")    
public class ${clsName} extends PermissionTestModuleBase {
    public $clsName(@NonNull Activity activity){ super(activity);}
    @Override
    public void start(Consumer<PermissionTestRunner.Result> callback){
        super.start(callback);
        logger.debug("Hello $typeSignature test case!");
    }
    private <T> T systemService(Class<T> clazz){
		return Objects.requireNonNull(getService(clazz),"[npe_system_service]"+clazz.getSimpleName());
	}
""".trimIndent())
                    testCases.forEach {
                    val params = mutableListOf<String>()
                    params.add("permission=${it.permissionLabel}")
                    if(it.sdkMin>0){
                        params.add("sdkMin=${it.sdkMin}")
                    }
                    if(it.sdkMax<100){
                        params.add("sdkMax=${it.sdkMax}")
                    }
                    if(it.isCustom){
                        params.add("customCase=true")
                    }
                    val params_ = params.joinToString()
                    var impl = it.impl.trimIndent()
                    //println(impl)
                    if(impl.startsWith("() -> {") && impl.endsWith("}")){
                        impl = impl.removePrefix("() -> {").removeSuffix("}")
                        impl = impl.trimIndent()
                        impl = impl.prependIndent("\t\t")
                        //println("here")
                    }
                    if(impl.startsWith("() -> ") && !impl.endsWith("}") && impl.endsWith(")")) {
                        impl = impl.removePrefix("() -> ")
                        impl = impl + ";"
                        impl = impl.trimIndent()
                        impl = impl.prependIndent("\t\t")
                        //println("here2")
                    }
                        fout.appendText(""" 
    @PermissionTest(${params_})
    public void test${it.permissionLabelCamel}(){
${impl}
    }
""")
                    }
                    fout.appendText("}\r\n")//Close class declarations
                    testCases.clear()
                    testCases = mutableListOf<TestCaseSource>()
                }


                //println("All Test Cases=>"+ testCases.size)

                // Read the source code
                /*
                val formatted = Roaster.format(content)
                val javaUnit = Roaster.parseUnit(formatted);

                val myClass: JavaClassSource = javaUnit.getGoverningType()
                myClass.methods.forEach { method->

                    //val text = CharStreams.fromString("hello world")
                    //val lexer = Java20Lexer(text)
                    //m.body
                    println(method.body)
                    if(method.body != null && method.body.length>0) {


                        //val stream = CommonTokenStream(lexer)
                        //parser.compilationUnit()
                        /*lexer.channelNames.forEach {
                            println(it)
                        }*/
                        /*lexer.allTokens.forEach {
                            println(it.text)
                        }*/
                        //println(lexer.channelNames.toString())

                    }

                }*/
                //println(myClass.methods[0].body)
            }
        }

    }
}