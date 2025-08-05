package org.wei.metabaseproxy.msg;

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.project.Project;

/**
 * @description:
 * @author: deanwanghewei@gmail.com
 * @date: 2024年09月30日 15:44
 */
public class AlertNotifier {
    private static final String NOTIFICATION_GROUP_ID = "Metabase Proxy Notifications Balloon";

    public static void notifyInfo(String title, String content) {
        Notification notification = new Notification(NOTIFICATION_GROUP_ID, title, content, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification);
    }

    /**
     * 非阻断式消息提醒, 会自动消失.因为设置了 displayType="BALLOON"
     *
     * @param project 配置了这个项目,可以在右下角弹出消息
     * @param title   提醒框标题
     * @param content 提醒内容
     */
    public static void notifyInfo(Project project, String title, String content) {
        Notification notification = new Notification(NOTIFICATION_GROUP_ID, title, content, NotificationType.INFORMATION);
        Notifications.Bus.notify(notification, project);
    }

    /**
     * 错误提醒
     *
     * @param title   提醒框标题
     * @param content 提醒内容
     */
    public static void notifyError(String title, String content) {
        Notification notification = new Notification(NOTIFICATION_GROUP_ID, title, content, NotificationType.ERROR);
        Notifications.Bus.notify(notification);
    }

    /**
     * 错误提醒
     *
     * @param project 配置了这个项目,可以在右下角弹出消息
     * @param title   提醒框标题
     * @param content 提醒内容
     */
    public static void notifyError(Project project, String title, String content) {
        Notification notification = new Notification(NOTIFICATION_GROUP_ID, title, content, NotificationType.ERROR);
        Notifications.Bus.notify(notification, project);
    }

    /**
     * 告警提醒
     *
     * @param title   提醒框标题
     * @param content 提醒内容
     */
    public static void notifyWarning(String title, String content) {
        Notification notification = new Notification(NOTIFICATION_GROUP_ID, title, content, NotificationType.WARNING);
        Notifications.Bus.notify(notification);
    }

    /**
     * 告警提醒
     *
     * @param project 配置了这个项目,可以在右下角弹出消息
     * @param title   提醒框标题
     * @param content 提醒内容
     */
    public static void notifyWarning(Project project, String title, String content) {
        Notification notification = new Notification(NOTIFICATION_GROUP_ID, title, content, NotificationType.WARNING);
        Notifications.Bus.notify(notification, project);
    }
}
