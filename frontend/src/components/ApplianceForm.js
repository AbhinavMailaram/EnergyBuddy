import React, { useState } from 'react';
import { applianceService } from '../services/apiService';
import './Components.css';

const ApplianceForm = ({ onApplianceAdded }) => {
  const [formData, setFormData] = useState({
    name: '',
    powerRatingWatts: '',
    category: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      await applianceService.create({
        ...formData,
        powerRatingWatts: parseFloat(formData.powerRatingWatts),
      });
      setFormData({ name: '', powerRatingWatts: '', category: '' });
      onApplianceAdded();
    } catch (err) {
      setError(err.response?.data?.error || 'Failed to add appliance');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="form-container">
      {error && <div className="error-message">{error}</div>}
      <form onSubmit={handleSubmit}>
        <div className="form-group">
          <label>Appliance Name *</label>
          <input
            type="text"
            name="name"
            value={formData.name}
            onChange={handleChange}
            placeholder="e.g., Refrigerator"
            required
          />
        </div>
        <div className="form-group">
          <label>Power Rating (Watts) *</label>
          <input
            type="number"
            name="powerRatingWatts"
            value={formData.powerRatingWatts}
            onChange={handleChange}
            placeholder="e.g., 150"
            step="0.01"
            min="0"
            required
          />
        </div>
        <div className="form-group">
          <label>Category</label>
          <select name="category" value={formData.category} onChange={handleChange}>
            <option value="">Select Category</option>
            <option value="Kitchen">Kitchen</option>
            <option value="Heating/Cooling">Heating/Cooling</option>
            <option value="Entertainment">Entertainment</option>
            <option value="Lighting">Lighting</option>
            <option value="Other">Other</option>
          </select>
        </div>
        <button type="submit" disabled={loading} className="primary-btn">
          {loading ? 'Adding...' : 'Add Appliance'}
        </button>
      </form>
    </div>
  );
};

export default ApplianceForm;
