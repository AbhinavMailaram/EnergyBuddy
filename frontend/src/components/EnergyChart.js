import React from 'react';
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer,
} from 'recharts';
import './Components.css';

const EnergyChart = ({ data }) => {
  if (!data || data.length === 0) {
    return (
      <div className="empty-state">
        <p>No energy consumption data available. Start logging your appliance usage!</p>
      </div>
    );
  }

  // Aggregate data by date
  const aggregatedData = data.reduce((acc, item) => {
    const date = item.loggedDate;
    if (!acc[date]) {
      acc[date] = {
        date: date,
        totalKwh: 0,
        totalWh: 0,
      };
    }
    acc[date].totalKwh += item.energyKwh || 0;
    acc[date].totalWh += item.energyWh || 0;
    return acc;
  }, {});

  const chartData = Object.values(aggregatedData)
    .sort((a, b) => new Date(a.date) - new Date(b.date))
    .slice(-30); // Last 30 days

  return (
    <div className="chart-container">
      <ResponsiveContainer width="100%" height={300}>
        <LineChart data={chartData}>
          <CartesianGrid strokeDasharray="3 3" />
          <XAxis
            dataKey="date"
            tickFormatter={(date) => {
              const d = new Date(date);
              return `${d.getMonth() + 1}/${d.getDate()}`;
            }}
          />
          <YAxis label={{ value: 'kWh', angle: -90, position: 'insideLeft' }} />
          <Tooltip
            formatter={(value) => `${value.toFixed(2)} kWh`}
            labelFormatter={(label) => `Date: ${label}`}
          />
          <Legend />
          <Line
            type="monotone"
            dataKey="totalKwh"
            stroke="#667eea"
            strokeWidth={2}
            name="Energy Consumption"
          />
        </LineChart>
      </ResponsiveContainer>

      <div className="chart-summary">
        <h3>Consumption by Appliance</h3>
        <ResponsiveContainer width="100%" height={250}>
          <BarChart
            data={getApplianceData(data)}
            margin={{ top: 20, right: 30, left: 20, bottom: 5 }}
          >
            <CartesianGrid strokeDasharray="3 3" />
            <XAxis dataKey="name" />
            <YAxis label={{ value: 'kWh', angle: -90, position: 'insideLeft' }} />
            <Tooltip formatter={(value) => `${value.toFixed(2)} kWh`} />
            <Bar dataKey="totalKwh" fill="#667eea" />
          </BarChart>
        </ResponsiveContainer>
      </div>
    </div>
  );
};

const getApplianceData = (data) => {
  const aggregated = data.reduce((acc, item) => {
    const name = item.applianceName;
    if (!acc[name]) {
      acc[name] = {
        name: name,
        totalKwh: 0,
      };
    }
    acc[name].totalKwh += item.energyKwh || 0;
    return acc;
  }, {});

  return Object.values(aggregated).sort((a, b) => b.totalKwh - a.totalKwh);
};

export default EnergyChart;
