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

        /*val ccp = project.configurations;//etByName("implementation")
        ccp.forEach{
            println(it.name)
        }*/

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
        val testCases = mutableListOf<TestCaseSource>()
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
                    val camel_ = permission_.caseFormat(UPPER_UNDERSCORE, LOWER_CAMEL)
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
                    testCases.add(testCS)
                    //println(testCS)
                }
            }
            /*
            override fun enterArguments(ctx: JavaParser.ArgumentsContext?) {
                super.enterArguments(ctx)
                val exprList = ctx?.expressionList()
                if(exprList != null){
                    println("parent:"+ctx.parent.text)
                    var i=0
                    exprList.expression()?.forEach {
                        val expr:String = getFullText(it)
                        println("\tinner expr($i)=>" + expr)
                        i++
                    }
                }
            }*/
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

        File(src_main_java).walk().forEach {
            if (it.extension == "java" &&
                it.nameWithoutExtension.endsWith("PermissionTester")) {
                println(it.nameWithoutExtension)

                val ins: InputStream = it.inputStream()
                val content = ins.readBytes().toString(Charset.defaultCharset())

                val lexer = JavaLexer(CharStreams.fromString(content))
                val parser = JavaParser(CommonTokenStream(lexer))
                val walker = ParseTreeWalker()
                walker.walk(AstListener(),parser.compilationUnit())

                println("All Test Cases=>"+ testCases.size)
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