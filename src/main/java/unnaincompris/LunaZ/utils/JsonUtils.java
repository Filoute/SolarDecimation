package unnaincompris.LunaZ.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.Getter;
import unnaincompris.LunaZ.utils.Gson.ListParameterizedType;

import java.io.File;
import java.util.ArrayList;

public class JsonUtils {
    @Getter private static final Gson gson =
        new GsonBuilder()
        .setPrettyPrinting()
        .excludeFieldsWithoutExposeAnnotation()
        //.registerTypeAdapter(Objective.class, new ObjectiveAdapter())
        .create();

    public static <T> T getData(File file, Class<T> type) {
        return gson.fromJson(FileUtils.readWholeFile(file), type);
    }

    public static <T> ArrayList<T> getDataList(File file, Class<T> type) {
        return gson.fromJson(FileUtils.readWholeFile(file), new ListParameterizedType(type));
    }

    public static void writeData(File file, Object clazz) {
        FileUtils.writeFile(file, gson.toJson(clazz));
    }

    public static File getOrCreateJson(File parent, String fileName) {
        return FileUtils.getOrCreateFile(parent, fileName + ".json");
    }

    public static boolean deleteJson(File parent, String fileName) {
        return FileUtils.deleteFile(parent, fileName + ".json");
    }
}
