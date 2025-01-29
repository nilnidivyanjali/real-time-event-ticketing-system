import { Component, OnDestroy, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';

 import SockJS from 'sockjs-client';
 import * as Stomp from 'stompjs';
import { IMessageResponse } from '../../models/messageResponse.interface';

@Component({
  selector: 'app-system-logs', // Updated selector to match the template usage
  standalone: true,
  templateUrl: './system-logs.component.html',
  styleUrls: ['./system-logs.component.css'],
  imports: [CommonModule],
})
export class SystemLogsComponent implements OnInit, OnDestroy {
  logs: IMessageResponse[] = []; // Stores the fetched logs
  private socketClient: Stomp.Client | null = null;
  

  
  ngOnDestroy(): void {
    throw new Error('Method not implemented.');
  }

  ngOnInit() {
    
    this.connectWebSocket();
    // this.ticketService.getLogs().subscribe({
    //   next: (data) => {
    //     this.logs = Array.isArray(data) ? data : ['Unexpected data format'];
    //   },
    //   error: (err) => {
    //     console.error('Error fetching logs', err);
    //     this.logs = ['Error fetching logs. Please try again later.'];
    //   },
    // });
  }
  connectWebSocket() {
    const ws = new SockJS('http://localhost:8080/chat');
    this.socketClient = Stomp.over(ws);
    this.socketClient.connect({}, () => {
     
      this.socketClient?.subscribe('/topic/messages', (message: { body: string }) => {
        const msgObj:IMessageResponse = JSON.parse(message.body);
        this.logs.push(msgObj);
        //this.addMessageToLogs(msgObj);
      });
    });
  }
  addMessageToLogs(msgObj: IMessageResponse) {
    console.log(msgObj);
    
    this.logs = [...this.logs, msgObj];
  }
}