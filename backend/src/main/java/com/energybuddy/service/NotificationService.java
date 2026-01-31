package com.energybuddy.service;

import com.energybuddy.dto.NotificationResponse;
import com.energybuddy.model.Notification;
import com.energybuddy.model.User;
import com.energybuddy.repository.NotificationRepository;
import com.energybuddy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private UserRepository userRepository;

    private User getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public void createNotification(User user, String message, String type) {
        Notification notification = new Notification();
        notification.setUser(user);
        notification.setMessage(message);
        notification.setType(type);
        notification.setIsRead(false);
        notificationRepository.save(notification);
    }

    public List<NotificationResponse> getUserNotifications() {
        User user = getCurrentUser();
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public List<NotificationResponse> getUnreadNotifications() {
        User user = getCurrentUser();
        return notificationRepository.findByUserIdAndIsReadFalseOrderByCreatedAtDesc(user.getId())
                .stream().map(this::mapToResponse).collect(Collectors.toList());
    }

    public void markAsRead(Long notificationId) {
        User user = getCurrentUser();
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found"));

        if (!notification.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        notification.setIsRead(true);
        notificationRepository.save(notification);
    }

    public Map<String, Object> getNotificationStats() {
        User user = getCurrentUser();
        Long unreadCount = notificationRepository.countByUserIdAndIsReadFalse(user.getId());
        
        Map<String, Object> stats = new HashMap<>();
        stats.put("unreadCount", unreadCount);
        return stats;
    }

    private NotificationResponse mapToResponse(Notification notification) {
        return new NotificationResponse(
                notification.getId(),
                notification.getMessage(),
                notification.getType(),
                notification.getIsRead(),
                notification.getCreatedAt()
        );
    }
}
