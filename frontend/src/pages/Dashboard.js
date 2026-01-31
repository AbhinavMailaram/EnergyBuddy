import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import {
  applianceService,
  energyService,
  suggestionService,
  notificationService,
} from '../services/apiService';
import ApplianceList from '../components/ApplianceList';
import ApplianceForm from '../components/ApplianceForm';
import EnergyLogForm from '../components/EnergyLogForm';
import EnergyChart from '../components/EnergyChart';
import Suggestions from '../components/Suggestions';
import Notifications from '../components/Notifications';
import './Dashboard.css';

const Dashboard = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [appliances, setAppliances] = useState([]);
  const [consumption, setConsumption] = useState([]);
  const [suggestions, setSuggestions] = useState([]);
  const [notifications, setNotifications] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [activeTab, setActiveTab] = useState('overview');

  useEffect(() => {
    loadData();
  }, []);

  const loadData = async () => {
    try {
      setLoading(true);
      setError('');
      
      const [appliancesData, consumptionData, suggestionsData, notificationsData] = 
        await Promise.all([
          applianceService.getAll(),
          energyService.getTrends(),
          suggestionService.getSuggestions(),
          notificationService.getUnread(),
        ]);

      setAppliances(appliancesData || []);
      setConsumption(consumptionData || []);
      setSuggestions(suggestionsData || []);
      setNotifications(notificationsData || []);
    } catch (err) {
      setError('Failed to load data. Please try again.');
      console.error('Error loading data:', err);
    } finally {
      setLoading(false);
    }
  };

  const handleApplianceAdded = () => {
    loadData();
  };

  const handleApplianceDeleted = () => {
    loadData();
  };

  const handleEnergyLogged = () => {
    loadData();
  };

  const handleLogout = () => {
    logout();
    navigate('/login');
  };

  if (loading) {
    return (
      <div className="dashboard">
        <div className="loading">Loading...</div>
      </div>
    );
  }

  return (
    <div className="dashboard">
      <header className="dashboard-header">
        <div className="header-content">
          <h1>Energy Buddy</h1>
          <div className="header-right">
            <span className="user-name">Welcome, {user?.username}!</span>
            <button onClick={handleLogout} className="logout-btn">
              Logout
            </button>
          </div>
        </div>
      </header>

      {error && <div className="error-banner">{error}</div>}

      <div className="dashboard-tabs">
        <button
          className={activeTab === 'overview' ? 'active' : ''}
          onClick={() => setActiveTab('overview')}
        >
          Overview
        </button>
        <button
          className={activeTab === 'appliances' ? 'active' : ''}
          onClick={() => setActiveTab('appliances')}
        >
          Appliances
        </button>
        <button
          className={activeTab === 'log' ? 'active' : ''}
          onClick={() => setActiveTab('log')}
        >
          Log Energy
        </button>
        <button
          className={activeTab === 'suggestions' ? 'active' : ''}
          onClick={() => setActiveTab('suggestions')}
        >
          Suggestions
        </button>
        <button
          className={activeTab === 'notifications' ? 'active' : ''}
          onClick={() => setActiveTab('notifications')}
        >
          Notifications {notifications.length > 0 && <span className="badge">{notifications.length}</span>}
        </button>
      </div>

      <div className="dashboard-content">
        {activeTab === 'overview' && (
          <div className="overview-grid">
            <div className="card">
              <h2>Energy Consumption Trends</h2>
              <EnergyChart data={consumption} />
            </div>
            <div className="card">
              <h2>Quick Stats</h2>
              <div className="stats-grid">
                <div className="stat-item">
                  <div className="stat-value">{appliances.length}</div>
                  <div className="stat-label">Appliances</div>
                </div>
                <div className="stat-item">
                  <div className="stat-value">{consumption.length}</div>
                  <div className="stat-label">Logs</div>
                </div>
                <div className="stat-item">
                  <div className="stat-value">
                    {consumption.reduce((sum, c) => sum + (c.energyKwh || 0), 0).toFixed(2)}
                  </div>
                  <div className="stat-label">Total kWh</div>
                </div>
              </div>
            </div>
          </div>
        )}

        {activeTab === 'appliances' && (
          <div className="appliances-grid">
            <div className="card">
              <h2>Add New Appliance</h2>
              <ApplianceForm onApplianceAdded={handleApplianceAdded} />
            </div>
            <div className="card">
              <h2>Your Appliances</h2>
              <ApplianceList
                appliances={appliances}
                onApplianceDeleted={handleApplianceDeleted}
              />
            </div>
          </div>
        )}

        {activeTab === 'log' && (
          <div className="card">
            <h2>Log Energy Consumption</h2>
            <EnergyLogForm
              appliances={appliances}
              onEnergyLogged={handleEnergyLogged}
            />
          </div>
        )}

        {activeTab === 'suggestions' && (
          <div className="card">
            <h2>Energy-Saving Suggestions</h2>
            <Suggestions suggestions={suggestions} />
          </div>
        )}

        {activeTab === 'notifications' && (
          <div className="card">
            <h2>Notifications</h2>
            <Notifications
              notifications={notifications}
              onNotificationRead={loadData}
            />
          </div>
        )}
      </div>
    </div>
  );
};

export default Dashboard;
