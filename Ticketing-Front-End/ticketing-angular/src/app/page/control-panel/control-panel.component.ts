import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import axios from 'axios';
import { ConfigurationSettingsComponent } from '../configuration-settings/configuration-settings.component';



@Component({
  selector: 'app-control-panel',
  standalone: true,
  templateUrl: './control-panel.component.html',
  styleUrls: ['./control-panel.component.css'],
  imports: [CommonModule]
})
export class ControlPanelComponent {
  private baseURL:string;
  constructor() {
    this.baseURL = 'http://localhost:8080/api';
    
    
  }

  async start() {
   
    try {
      const endpoint:string ='start';
      const response = await axios.post(`${this.baseURL}/${endpoint}`,{
        headers: { 
        'Content-Type': 'application/json'
        },
      });
      const data2 = await response.data; // Axios automatically parses 
      console.log(JSON.stringify(data2));
       alert(data2);
      
    } catch (error) {
      alert('Error startong service');
      console.error('Error fetching data', error);
      throw error;
    }
  }

  async stop() {
    try {
      const endpoint:string ='stop';
      const response = await axios.post(`${this.baseURL}/${endpoint}`,{
        headers: { 
        'Content-Type': 'application/json'
        },
      });
      const data2 = await response.data; // Axios automatically parses 
      console.log(JSON.stringify(data2));
       alert(data2);
      
    } catch (error) {
      alert('Error startong service');
      console.error('Error fetching data', error);
      throw error;
    }
  }

 async  exit() {
  try {
    const endpoint:string ='exit';
    const response = await axios.post(`${this.baseURL}/${endpoint}`,{
      headers: { 
      'Content-Type': 'application/json'
      },
    });
    const data2 = await response.data; // Axios automatically parses 
    console.log(JSON.stringify(data2));
     alert(data2);
    
  } catch (error) {
    alert('Error startong service');
    console.error('Error fetching data', error);
    throw error;
  }
  }

  test(){
    
    let data = JSON.stringify({
      "totalTickets": 100,
      "ticketReleaseRate": 5,
      "customerRetrievalRate": 3,
      "maxTicketCapacity": 200
    });

    let config = {
      method: 'post',
      maxBodyLength: Infinity,
      url: 'http://localhost:8080/api/configure',
      headers: { 
        'Content-Type': 'application/json'
      },
      data : data
    };

    axios.request(config)
    .then((response:any) => {
      console.log(JSON.stringify(response.data));
    })
    .catch((error:any) => {
      console.log(error);
    });

  }
}