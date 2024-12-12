import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class CommentsMain {

    public static void main(String[] args) throws IOException, InterruptedException {
        if (args.length < 2) {
            System.out.println("ERR:请输入备注文本！");
            return;
        }
        String filename = args[0];
        String comment = args[1];

        File file = new File(filename);
        if (!file.isDirectory()) {
            System.out.println("ERR:该目录不是文件夹！");
            return;
        }

        File ini = new File(filename + File.separator + "desktop.ini");
        if (ini.exists()) {
            writeComment(filename, comment);
        } else {
            writeCommentNewFile(filename, comment);
        }

        System.out.println("INFO:成功。");
    }

    private static void writeComment(String filename, String comment) throws IOException, InterruptedException {

        Process process = Runtime.getRuntime().exec("attrib -s -h " + filename + File.separator + "desktop.ini");

        Thread.sleep(1000);

        File ini = new File(filename + File.separator + "desktop.ini");

        Lines lines = new Lines();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(ini), "GBK"))) {
            String line;
            int num = 0;
            while ((line = br.readLine()) != null) {
                lines.addLine(new Line(num ++, line));
            }
        }

        Line info = lines.getLine("InfoTip");
        if (info != null) {
            info.replace("InfoTip=" + comment);
        } else {
            Line clazz = lines.getLine("ShellClassInfo");
            int clazzIndex = clazz.num;
            lines.add(clazzIndex + 1, new Line(clazzIndex + 1, "InfoTip=" + comment));
        }
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ini), "GBK"))) {
            for (Line l : lines.lines) {
                bw.write(l.content);
                bw.newLine();
            }
            bw.flush();
        }
        Runtime.getRuntime().exec("attrib +s +h " + ini.getPath());
    }

    private static void writeCommentNewFile(String filename, String comment) throws IOException {

        File ini = new File(filename + File.separator + "desktop.ini");
        File dir = ini.getParentFile();
        Runtime.getRuntime().exec("attrib +s " + dir.getPath());

        String[] s = new String[]{"[.ShellClassInfo]",
                "ConfirmFileOp=0",
                "InfoTip=" + comment};
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(ini), "GBK"))) {
            for (String ss : s) {
                bw.write(ss);
                bw.newLine();
            }
            bw.flush();
        }
        Runtime.getRuntime().exec("attrib +s +h " + ini.getPath());
    }

    private static class Lines {
        private List<Line> lines = new ArrayList<>();

        private void addLine(Line line) {
            lines.add(line);
        }

        private Line getLine(String name) {
            return lines.stream().filter(l -> l.content.contains(name)).findFirst().orElse(null);
        }

        private void add(int index, Line line) {
            lines.add(index, line);
        }
    }

    private static class Line {
        private int num;
        private String content;

        private Line(int num, String content) {
            this.num = num;
            this.content = content;
        }

        private void replace(String content) {
            this.content = content;
        }
    }
}
