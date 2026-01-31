import React, { useState } from 'react';
import { energyService } from '../services/apiService';
import './Components.css';

const EnergyLogForm = ({ appliances, onEnergyLogged }) => {
  const [formData, setFormData] = useState({
    applianceId: '',
    usageHours: '',
    loggedDate: new Date().toISOString().split('T')[0],
  });
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');
    setLoading(true);

    try {
      const result = await energyService.logConsumption({
        applianceId: parseInt(formData.applianceId),
        usageHours: parseFloat(formData.usageHours),
        loggedDate: formData.loggedDate,
      });

      setSuccess(
        `Energy logged: ${result.energyWh.toFixed(2)} Wh (${result.energyKwh.toFixed(3)} kWh)`
      );
      setFormData({
        applianceId: '',
        usageHours: '',
        loggedDate: new Date().toISOString().split('T')[0],
      });
      onEnergyLogged();
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to log energy consumption');
    } finally {
      setLoading(false);
    }
  };

  if (appliances.length === 0) {
    return (
      <div className="empty-state">
        <p>Please add appliances first before logging energy consumption.</p>
      </div>
    );
  }

  return (
    <div className="form-container">
      {error && <div className="error-message">{error}</div>}
      {success && <div className="success-message">{success}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Select Appliance *</label>
          <select
            name="applianceId"
            value={formData.applianceId}
            onChange={handleChange}
            required
          >
            <option value="">Choose an appliance</option>
            {appliances.map((appliance) => (
              <option key={appliance.id} value={appliance.id}>
                {appliance.name} ({appliance.powerRatingWatts}W)
              </option>
            ))}
          </select>
        </div>
        <div className="form-group">
          <label>Usage Hours *</label>
          <input
            type="number"
            name="usageHours"
            value={formData.usageHours}
            onChange={handleChange}
            placeholder="e.g., 5.5"
            step="0.1"
            min="0"
            required
          />
          <small>Enter the number of hours this appliance was used</small>
        </div>
        <div className="form-group">
          <label>Date *</label>
          <input
            type="date"
            name="loggedDate"
            value={formData.loggedDate}
            onChange={handleChange}
            max={new Date().toISOString().split('T')[0]}
            required
          />
        </div>
        <button type="submit" disabled={loading} className="primary-btn">
          {loading ? 'Logging...' : 'Log Energy Consumption'}
        </button>
      </form>
    </div>
  );
};

export default EnergyLogForm;
