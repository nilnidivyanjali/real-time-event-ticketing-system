import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms'; // Required for ngModel binding
import axios from 'axios';

@Component({
  selector: 'app-configuration-settings',
  standalone: true, // Indicates a standalone component
  templateUrl: './configuration-settings.component.html',
  styleUrls: ['./configuration-settings.component.css'],
  imports: [CommonModule, FormsModule], // Import CommonModule and FormsModule for functionality
})
export class ConfigurationSettingsComponent {
  // Configuration object to store form values

  private baseURL:string;
  constructor() {
    this.baseURL = 'http://localhost:8080/api';
    
    
  }
  configuration = {
    totalTickets: 0,
    ticketReleaseRate: 0,
    customerRetrievalRate: 0,
    maxTicketCapacity: 0,
  };

  /**
   * Handles form submission and displays the configuration data in an alert.
   */
  onSubmit() {
    if (this.validateConfiguration()) {
     
      this.start(
         this.configuration.totalTickets,
         this.configuration.ticketReleaseRate, 
         this.configuration.customerRetrievalRate,
         this.configuration.maxTicketCapacity
        );
    } else {
      alert('Please fill out all fields with valid values.');
    }
  }

  async start(totalTickets: number, ticketReleaseRate: number, customerRetrievalRate: number, maxTicketCapacity: number) {
    console.log("start");
    
    let data = JSON.stringify({
      "totalTickets": totalTickets,
      "ticketReleaseRate": ticketReleaseRate,
      "customerRetrievalRate": customerRetrievalRate,
      "maxTicketCapacity": maxTicketCapacity
    });
   
    try {
      const endpoint:string ='configure';
      const response = await axios.post(`${this.baseURL}/${endpoint}`,data,{
        headers: { 
        'Content-Type': 'application/json'
        },
      });
      const data2 = await response.data; // Axios automatically parses 
      console.log(JSON.stringify(data2));
      alert('Configuration Saved Successfully: ' + JSON.stringify(data2));
      
    } catch (error) {
      alert('Error startong service');
      console.error('Error fetching data', error);
      throw error;
    }
  }

  /**
   * V
   * 
   * alidates the configuration object to ensure all fields are filled with valid values.
   * @returns {boolean} - True if valid, false otherwise.
   */
  validateConfiguration(): boolean {
    return (
      this.configuration.totalTickets > 0 &&
      this.configuration.ticketReleaseRate > 0 &&
      this.configuration.customerRetrievalRate > 0 &&
      this.configuration.maxTicketCapacity > 0
    );
  }
}