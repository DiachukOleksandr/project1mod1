import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileService {
        public String read(Path path) {
            try {
               return Files.readString(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        public void write(Path path, String text) {
            try {
                 Files.writeString(path, text);
            } catch (IOException e) {
                 throw new RuntimeException(e);
            }
        }

        public Path addFileNameAnnotation(Path path, String annotation) {
            StringBuilder fileName = new StringBuilder(path.getFileName().toString());
            Path parentDir = path.getParent();
            if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0) {
            fileName.insert(fileName.lastIndexOf("."), annotation);
            return parentDir.resolve(fileName.toString());
            }
            return parentDir;
        }

}
