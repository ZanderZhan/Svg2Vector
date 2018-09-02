package Action;

import UI.GenerateResult;
import com.android.ide.common.vectordrawable.Svg2Vector;
import com.intellij.openapi.project.Project;
import com.intellij.util.io.PathKt;
import com.sun.jna.StringArray;
import utils.Logger;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;

public class Svg2VectorHelper {

    private Project mProject;
    private String mPrefix;
    private ArrayList<String> mSuccessList;
    private ArrayList<String> mFailList;
    private ArrayList<String> mSkipList;

    public Svg2VectorHelper(Project project) {
        mProject = project;
        mSuccessList = new ArrayList<String>();
        mFailList = new ArrayList<String>();
        mSkipList = new ArrayList<String>();
    }

    public void convertSvgToVector(String svgPath, Path targetPath, String prefix) {

        mPrefix = prefix;
        String[] sourcePaths = svgPath.split(";");
        for (String sourcePath : sourcePaths) {
            Path path = Paths.get(sourcePath);
            if(path != null) {
                if (checkPath(path)) {
                    iterateDirectory(path, targetPath);
                } else {
                    convert(path, targetPath);
                }
            }
        }

    }

    private void convert(Path file, Path targetPath) {

        if (!checkFile(file)) {
            Logger.getInstance().warn("skip " + file.getFileName() + " as it's not svg");
            mSkipList.add(file.getFileName().toString());
            return ;
        }
        if (!checkPath(targetPath)) {
            Logger.getInstance().error(targetPath.toString() + "is not directory");
        }
        try {
            String errlog = Svg2Vector.parseSvgToXml(file.toFile(), convertPathToFileStream(file, targetPath));

            if (errlog.contains("EXCEPTION")) {
                Logger.getInstance().error(errlog);
                mFailList.add(file.toFile().toString());
            } else {
                mSuccessList.add(file.toFile().toString());
            }

        } catch (IOException e) {
            Logger.getInstance().error("IOException "+e.getMessage());
            mFailList.add(file.toFile().toString());
        }


    }

    private boolean checkPath(Path path) {
        return PathKt.isDirectory(path);
    }

    private boolean checkFile(Path file) {
        return file.getFileName().toString().endsWith(".svg");
    }

    private FileOutputStream convertPathToFileStream(Path file, Path path) throws IOException {
        String base = mPrefix + path.toString() + File.separator;
        String fileName = file.toFile().getName();
        int index = fileName.indexOf(".");
        if (index != -1) {
            base += fileName.substring(0, index);
        }
        base += ".xml";
//        Logger.getInstance().info("try to generate file" + base);
        return new FileOutputStream(new File(base));
    }

    private void iterateDirectory(Path directory, Path target) {

        EnumSet<FileVisitOption> options = EnumSet.of(FileVisitOption.FOLLOW_LINKS);

        try {

            Files.walkFileTree(directory, options, Integer.MAX_VALUE, new FileVisitor<Path>() {

                @Override
                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    convert(file, target);
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    showResult();
                    return FileVisitResult.TERMINATE;
                }

            });

        } catch (IOException e){
            Logger.getInstance().error("IOException "+e.getMessage());
        }

    }

    private void showResult() {
        GenerateResult.main(mSuccessList, mFailList);
    }
}
