package at.ac.fhcampuswien.fhmdb;

import javafx.util.Callback;

import java.util.HashMap;
import java.util.Map;

public class ControllerFactory implements Callback<Class<?>, Object> {

    // Map zur Speicherung der Singleton-Instanzen
    private final Map<Class<?>, Object> controllerInstances = new HashMap<>();

    @Override
    public Object call(Class<?> controllerClass) {
        // Prüfen, ob bereits eine Instanz existiert
        if (controllerInstances.containsKey(controllerClass)) {
            return controllerInstances.get(controllerClass);
        }
        //Falls nicht, wird eine neue Instanz erstellt.
        try {
            // Neue Instanz erzeugen und speichern
            Object controller = controllerClass.getDeclaredConstructor().newInstance();
            controllerInstances.put(controllerClass, controller);
            System.out.println("Neue Instanz erstellt: " + controllerClass.getSimpleName());
            return controller;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
