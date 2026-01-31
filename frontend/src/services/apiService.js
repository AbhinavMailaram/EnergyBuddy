import api from './api';

export const authService = {
  register: async (userData) => {
    const response = await api.post('/auth/register', userData);
    if (response.data.token) {
      sessionStorage.setItem('token', response.data.token);
      sessionStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  },

  login: async (credentials) => {
    const response = await api.post('/auth/login', credentials);
    if (response.data.token) {
      sessionStorage.setItem('token', response.data.token);
      sessionStorage.setItem('user', JSON.stringify(response.data));
    }
    return response.data;
  },

  logout: () => {
    sessionStorage.removeItem('token');
    sessionStorage.removeItem('user');
  },

  getCurrentUser: () => {
    const userStr = sessionStorage.getItem('user');
    return userStr ? JSON.parse(userStr) : null;
  },

  isAuthenticated: () => {
    return !!sessionStorage.getItem('token');
  },
};

export const applianceService = {
  getAll: async () => {
    const response = await api.get('/appliances');
    return response.data;
  },

  create: async (applianceData) => {
    const response = await api.post('/appliances', applianceData);
    return response.data;
  },

  update: async (id, applianceData) => {
    const response = await api.put(`/appliances/${id}`, applianceData);
    return response.data;
  },

  delete: async (id) => {
    const response = await api.delete(`/appliances/${id}`);
    return response.data;
  },
};

export const energyService = {
  logConsumption: async (logData) => {
    const response = await api.post('/energy/log', logData);
    return response.data;
  },

  getConsumption: async () => {
    const response = await api.get('/energy/consumption');
    return response.data;
  },

  getTrends: async (startDate, endDate) => {
    let url = '/energy/trends';
    const params = new URLSearchParams();
    if (startDate) params.append('startDate', startDate);
    if (endDate) params.append('endDate', endDate);
    if (params.toString()) url += `?${params.toString()}`;
    
    const response = await api.get(url);
    return response.data;
  },
};

export const suggestionService = {
  getSuggestions: async () => {
    const response = await api.get('/suggestions');
    return response.data;
  },
};

export const notificationService = {
  getAll: async () => {
    const response = await api.get('/notifications');
    return response.data;
  },

  getUnread: async () => {
    const response = await api.get('/notifications/unread');
    return response.data;
  },

  getStats: async () => {
    const response = await api.get('/notifications/stats');
    return response.data;
  },

  markAsRead: async (id) => {
    const response = await api.post(`/notifications/${id}/mark-read`);
    return response.data;
  },
};
