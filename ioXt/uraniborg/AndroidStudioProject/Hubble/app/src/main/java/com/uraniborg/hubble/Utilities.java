//Copyright 2019 Uraniborg authors.
//
//Licensed under the Apache License, Version 2.0 (the "License");
//you may not use this file except in compliance with the License.
//You may obtain a copy of the License at
//
//http://www.apache.org/licenses/LICENSE-2.0
//
//Unless required by applicable law or agreed to in writing, software
//distributed under the License is distributed on an "AS IS" BASIS,
//WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
//See the License for the specific language governing permissions and
//limitations under the License.

package com.uraniborg.hubble;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.Signature;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import androidx.annotation.NonNull;

public class Utilities {
  public static final double Kilobyte = 1024.0;
  public static final double Megabyte = Kilobyte * 1024.0;
  public static final double Gigabyte = Megabyte * 1024.0;
  public static final double Terabyte = Gigabyte * 1024.0;


  /**
   * Computes the SHA256 digest given a string containing a path to a file.
   * @param context The execution context
   * @param pathToFile A string containing a path to the file of which its content is to be hashed.
   * @return A string representing the encoded SHA256 digest of the content of the file. A
   * <code>null</code> may be returned if any intermediate process failed.
   */
  @Nullable
  public static String computeSHA256DigestOfFile(Context context, String pathToFile) {
    final String TAG = "SHA256-File";
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA256");
      md.reset();
    } catch (NoSuchAlgorithmException e) {
      Log.e(TAG, "SHA256 isn't implemented on this device.");
      return null;
    }
    File f = new File(pathToFile);
    FileInputStream fis;
    try {
      fis = new FileInputStream(f);
    } catch (FileNotFoundException e) {
      Log.e(TAG, String.format("File %s is not found on the system!", f.getAbsolutePath()));
      return null;
    }
    DigestInputStream dis = new DigestInputStream(fis, md);
    long filesize = f.length();
    if (filesize == 0) {
      Log.e(TAG, String.format("%s is an empty file", pathToFile));
      return null;
    }

    // decide on buffer size based on device's memory class
    ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
    if (am == null) {
      Log.e(TAG, String.format("Error obtaining ActivityManager"));
      return null;
    }
    int bufferSize = (int) (am.isLowRamDevice() ? 512 : 8 * Kilobyte);
    bufferSize = (int) Math.min(filesize, bufferSize);

    byte[] buffer = new byte[bufferSize];

    try {
      while (dis.read(buffer) != -1) {}

    } catch (IOException e) {
      Log.e(TAG, String.format("Failed to read from %s due to %s", pathToFile, e.getMessage()));
      return null;
    }

    try {
      dis.close();
    } catch (IOException e) {
      Log.w(TAG, String.format("Failed to close DigestInputStream"));
    }

    return convertBytesToHexString(md.digest());
  }


  /**
   * Computes the SHA256 digest of an Android certificate object, known as signature.
   * @param certificate the 'signature' of which its hash is to be computed
   * @return A string representing the encoded SHA256 digest of @param certificate. A
   * <code>null</code> may be returned if any intermediate process failed.
   */
  @Nullable
  public static String computeSHA256DigestOfCertificate(Signature certificate) {
    final String TAG = "SHA256OfCert";
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("SHA256");
      md.reset();
    } catch (NoSuchAlgorithmException e) {
      Log.e(TAG, "SHA256 isn't implemented on this device.");
      return null;
    }
    md.update(certificate.toByteArray());

    // convert result to hex string
    return convertBytesToHexString(md.digest());
  }


  /**
   * Utility function to convert/encode bytes into hex string.
   * @param bytes The byte array to be converted.
   * @return A string corresponding to the @param bytes input.
   */
  @NotNull
  public static String convertBytesToHexString(@NonNull byte[] bytes) {
    StringBuilder sb = new StringBuilder();
    for (byte b : bytes) {
      sb.append(String.format("%02x", b));
    }
    return sb.toString();
  }

  /**
   * A helper method that wraps around the execution of a shell command and the handling of
   * exceptions and encapsulating command outputs to be delivered back to the caller via one
   * method call.
   * @param cmd The command to execute
   * @return An {@link ExecutionResult} instance that encapsulates the execution status and
   * output of the execution of the command. ALWAYS check the {@link ExecutionResult#exitCode}
   * or {@link ExecutionResult#exceptionTriggered} before consuming any other fields.
   */
  public static ExecutionResult executeInShell(@NotNull String cmd) {
    final String TAG = "executeInShell";
    ExecutionResult executionResult = new ExecutionResult();

    try {
      Process process = Runtime.getRuntime().exec(cmd.split("\\s+"));
      InputStream processInputStream = process.getInputStream();
      InputStream processErrorStream = process.getErrorStream();

      StreamConsumer outputStreamConsumer = new StreamConsumer(processInputStream, "stdout");
      StreamConsumer errorStreamConsumer = new StreamConsumer(processErrorStream, "stderr");

      Thread threadStdout = new Thread(outputStreamConsumer);
      Thread threadStderr = new Thread(errorStreamConsumer);
      threadStdout.start();
      threadStderr.start();

      executionResult.exitCode = process.waitFor();
      threadStdout.join();
      threadStderr.join();

      executionResult.stdOutStr = outputStreamConsumer.getOutput();
      executionResult.stdErrStr = errorStreamConsumer.getOutput();
    } catch (IOException e) {
      Log.e(TAG, String.format("Failed to execute: `%s` with error: %s", cmd, e.getMessage()));
      executionResult.exceptionTriggered = true;
      executionResult.exceptionMessage = e.getMessage();
      executionResult.exitCode = null;
    } catch (InterruptedException e) {
      Log.e(TAG, String.format("Executing `%s` was interrupted with error: %s", cmd,
          e.getMessage()));
      executionResult.exceptionTriggered = true;
      executionResult.exceptionMessage = e.getMessage();
      executionResult.exitCode = null;
    }

    return executionResult;
  }


  /**
   * Get a storage directory suitable to write result files to, without acquiring additional
   * permissions.
   * @param context The current execution context.
   * @return a {@link File} object that points to the desired directory. NOTE: Remember to check
   *         if the directory exists before opening for read/write.
   */
  public static File getResultStorageDirectory(@NotNull Context context) {
    File rootDir = context.getExternalFilesDir(null);
    File resultDir = new File(rootDir, "results");
    return resultDir;
  }

  /**
   * Utility function to help flush contents to file within the app's external data dir.
   * @param context The execution context.
   * @param filename The name of the file to write to.
   * @param content A string containing the data to write.
   * @param append A boolean on whether to write to the end of the file or to overwrite the file.
   * @return <code>true</code> if the write operation is successful. <code>false</code> otherwise.
   */
  public static boolean writeToFile(@NotNull Context context,
                                    @NotNull String filename, String content,
                                    boolean append) {
    final String TAG = "writeToFile";
    File resultDir = getResultStorageDirectory(context);
    if (!resultDir.exists()) {
      resultDir.mkdirs();
    }
    File dataFile = new File(resultDir, filename);

    try (FileOutputStream fos = new FileOutputStream(dataFile, append)) {
      fos.write(content.getBytes());
    } catch (FileNotFoundException e) {
      Log.e(TAG, String.format("File %s is not found for writing: %s", dataFile.getAbsolutePath(),
          e.getMessage()));
      return false;
    } catch (IOException e) {
      Log.e(TAG, String.format("Error while writing to file: %s", dataFile.getAbsolutePath(),
          e.getMessage()));
      return false;
    }

    return true;
  }


  /**
   * Utility function to help recursively list all files in a given root directory
   * @param root A {@link String} object containing a valid root directory.
   * @param includeSymlink A boolean determining whether or not symlinks were to be counted.
   * @return A {@link List<File>} object containing all the non-directory files in the given root
   *         directory.
   */
  public static List<File> getAllFilesInDirectory(@NonNull String root, boolean includeSymlink) {
    final String TAG = "getAllFiles";
    File rootPath = new File(root);
    List<File> resultList = new ArrayList<>();
    Set<File> resultSet = new HashSet<>();

    File[] files = rootPath.listFiles();
    if (files == null) {
      return resultList;
    }

    for (File file : files) {
      if (file.isFile()) {
        if (includeSymlink) {
          resultList.add(file);
          continue;
        }

        try {
          resultSet.add(file.getCanonicalFile());
        } catch (IOException e) {
          Log.e(TAG, String.format("Failed to get canonical file for %s: %s",
              file.getAbsolutePath(), e.getMessage()));
          continue;
        }
      } else if (file.isDirectory()) {
        if (includeSymlink) {
          resultList.addAll(getAllFilesInDirectory(file.getAbsolutePath(), includeSymlink));
        } else {
          resultSet.addAll(getAllFilesInDirectory(file.getAbsolutePath(), includeSymlink));
        }
      }
    }

    if (includeSymlink) {
      return resultList;
    } else {
      return new ArrayList<>(resultSet);
    }
  }

  /**
   * Implementing a stream consumer to handle stream consumption asynchronously. This helps to avoid
   * deadlock/blocking of the main thread.
   */
  public static class StreamConsumer implements Runnable {
    public final String TAG = "StreamConsumer";
    InputStream inputStream;
    String streamType;
    boolean exceptionTriggered;
    String exceptionMessage;
    String result;

    public StreamConsumer(InputStream inputStream, String streamType) {
      this.inputStream = inputStream;
      this.streamType = streamType;
    }

    @Override
    public void run() {
      BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

      StringBuilder resultBuffer = new StringBuilder();
      String line;
      try {
        while ((line = br.readLine()) != null) {
          resultBuffer.append(line + "\n");   // need to append \n because readline strips that off
        }
      } catch (IOException e) {
        Log.e(TAG, String.format("Failed to read from %s stream.", this.streamType));
        exceptionTriggered = true;
        exceptionMessage = e.getMessage();
      }
      result = resultBuffer.toString();
    }

    public String getOutput() {
      return result;
    }
  }

}
