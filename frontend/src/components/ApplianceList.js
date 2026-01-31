import React, { useState } from 'react';
import { applianceService } from '../services/apiService';
import './Components.css';

const ApplianceList = ({ appliances, onApplianceDeleted }) => {
  const [deletingId, setDeletingId] = useState(null);
  const [error, setError] = useState('');

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this appliance?')) {
      return;
    }

    setError('');
    setDeletingId(id);

    try {
      await applianceService.delete(id);
      onApplianceDeleted();
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to delete appliance');
    } finally {
      setDeletingId(null);
    }
  };

  if (appliances.length === 0) {
    return (
      <div className="empty-state">
        <p>No appliances registered yet. Add your first appliance to get started!</p>
      </div>
    );
  }

  return (
    <div className="list-container">
      {error && <div className="error-message">{error}</div>}
      <div className="appliance-list">
        {appliances.map((appliance) => (
          <div key={appliance.id} className="appliance-item">
            <div className="appliance-info">
              <h3>{appliance.name}</h3>
              <p className="appliance-power">{appliance.powerRatingWatts}W</p>
              {appliance.category && (
                <span className="appliance-category">{appliance.category}</span>
              )}
            </div>
            <button
              onClick={() => handleDelete(appliance.id)}
              disabled={deletingId === appliance.id}
              className="delete-btn"
            >
              {deletingId === appliance.id ? 'Deleting...' : 'Delete'}
            </button>
          </div>
        ))}
      </div>
    </div>
  );
};

export default ApplianceList;
