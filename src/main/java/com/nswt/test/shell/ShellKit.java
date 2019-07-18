package com.nswt.test.shell;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class ShellKit {
    /**
     * 运行shell脚本
     * @param shell 需要运行的shell脚本
    */
    public static  void execShell(String shell) {
        try {
            Runtime rt = Runtime.getRuntime();
            rt.exec(shell);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 运行shell
     *
     * @param shStr
     * 需要执行的shell
     * @return
     * @throws IOException
     * 注:如果sh中含有awk,一定要按new String[]{"/bin/sh","-c",shStr}写,才可以获得流.
     */
    public static  List<String> runShell(String shStr) throws Exception {
        List<String> strList = new ArrayList<String>();
        System.out.println(ShellKit.class.getResource("/"));
        Process process;
        process = Runtime.getRuntime().exec(new String[] {"/bin/sh", shStr, "root@10.9.248.7", "~/bin/monit stop ods-service-basic-test"},null,null);
        InputStreamReader ir = new InputStreamReader(process
                .getInputStream());
        LineNumberReader input = new LineNumberReader(ir);
        String line;
        process.waitFor();
        while ((line = input.readLine()) != null) {
            strList.add(line);
        }

        return strList;
    }
    
    public static void main(String[] args) {
    	String fileName = args[0];
		try {
//			List<String> shellResult = runShell("/usr/local/soc-ods-tools-project-test/test.sh");
			List<String> shellResult = runShell(fileName);
			System.out.println(shellResult);
			System.out.println(shellResult.get(shellResult.size()-1));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}