import React from 'react';
import './Components.css';

const Suggestions = ({ suggestions }) => {
  if (!suggestions || suggestions.length === 0) {
    return (
      <div className="empty-state">
        <p>No suggestions available yet. Keep logging your energy consumption!</p>
      </div>
    );
  }

  const getPriorityClass = (priority) => {
    switch (priority) {
      case 'high':
        return 'priority-high';
      case 'medium':
        return 'priority-medium';
      case 'low':
        return 'priority-low';
      default:
        return 'priority-info';
    }
  };

  const getPriorityIcon = (priority) => {
    switch (priority) {
      case 'high':
        return '!';
      case 'medium':
        return '~';
      case 'low':
        return 'i';
      default:
        return 'i';
    }
  };

  return (
    <div className="suggestions-container">
      {suggestions.map((suggestion, index) => (
        <div key={index} className={`suggestion-card ${getPriorityClass(suggestion.priority)}`}>
          <div className="suggestion-header">
            <span className="suggestion-icon">{getPriorityIcon(suggestion.priority)}</span>
            <h3>{suggestion.title}</h3>
          </div>
          <p>{suggestion.description}</p>
          {suggestion.savings && (
            <div className="suggestion-savings">
              <strong>Savings: {suggestion.savings}</strong>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default Suggestions;
