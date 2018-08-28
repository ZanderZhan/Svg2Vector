package utils;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;

public class Logger {

    private String groupId;
    private final static Logger instance = new Logger("Svg2Vector");

    private Logger(String groupId) {
        this.groupId = groupId;
    }

    public static Logger getInstance() {
        return instance;
    }

    public void info(String content) {
        Notification notification = new Notification(groupId, groupId, content, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

    public void warn(String content) {
        Notification notification = new Notification(groupId, groupId, content, NotificationType.WARNING);
        Notifications.Bus.notify(notification);
    }

    public void error(String content) {
        Notification notification = new Notification(groupId, groupId, content, NotificationType.WARNING);
        Notifications.Bus.notify(notification);
    }
}
