import React, { useState } from 'react';
import { notificationService } from '../services/apiService';
import './Components.css';

const Notifications = ({ notifications, onNotificationRead }) => {
  const [markingRead, setMarkingRead] = useState(null);

  const handleMarkAsRead = async (id) => {
    setMarkingRead(id);
    try {
      await notificationService.markAsRead(id);
      onNotificationRead();
    } catch (err) {
      console.error('Error marking notification as read:', err);
    } finally {
      setMarkingRead(null);
    }
  };

  if (!notifications || notifications.length === 0) {
    return (
      <div className="empty-state">
        <p>No new notifications. You're all caught up!</p>
      </div>
    );
  }

  const getNotificationClass = (type) => {
    switch (type) {
      case 'HIGH_CONSUMPTION':
        return 'notification-warning';
      case 'SUGGESTION':
        return 'notification-info';
      default:
        return 'notification-default';
    }
  };

  const getNotificationIcon = (type) => {
    switch (type) {
      case 'HIGH_CONSUMPTION':
        return '⚠️';
      case 'SUGGESTION':
        return '💡';
      default:
        return 'ℹ️';
    }
  };

  return (
    <div className="notifications-container">
      {notifications.map((notification) => (
        <div
          key={notification.id}
          className={`notification-item ${getNotificationClass(notification.type)}`}
        >
          <div className="notification-content">
            <span className="notification-icon">
              {getNotificationIcon(notification.type)}
            </span>
            <div className="notification-text">
              <p>{notification.message}</p>
              <small>
                {new Date(notification.createdAt).toLocaleString()}
              </small>
            </div>
          </div>
          {!notification.isRead && (
            <button
              onClick={() => handleMarkAsRead(notification.id)}
              disabled={markingRead === notification.id}
              className="mark-read-btn"
            >
              {markingRead === notification.id ? '...' : 'Mark Read'}
            </button>
          )}
        </div>
      ))}
    </div>
  );
};

export default Notifications;
