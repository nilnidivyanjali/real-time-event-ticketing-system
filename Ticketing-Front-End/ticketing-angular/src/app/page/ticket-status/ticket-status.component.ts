import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { TicketService } from '../../services/ticket-service.service';

@Component({
  selector: 'app-ticket-status',
  standalone: true,
  templateUrl: './ticket-status.component.html',
  styleUrls: ['./ticket-status.component.css'],
  imports: [CommonModule]
})
export class TicketStatusComponent implements OnInit {
  ticketStatus: any; // Updated property to store ticket status

  constructor(private ticketService: TicketService) {}

  ngOnInit() {
    // Subscribe to the getTicketStatus method of the TicketService
    this.ticketService.getTicketStatus().subscribe({
      next: (data) => {
        this.ticketStatus = data; // Assign the fetched status to the ticketStatus property
      },
      error: (err) => {
        console.error('Error fetching ticket status', err); // Handle any errors
      }
    });
  }
}