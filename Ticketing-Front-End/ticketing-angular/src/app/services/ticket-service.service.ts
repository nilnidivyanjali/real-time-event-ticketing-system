import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root', // Ensures this service is available globally in the app
})
export class TicketService {
  private readonly apiUrl = 'http://localhost:8080/api'; // Base URL for the backend

  constructor(private readonly http: HttpClient) {
    this.http=http;
  }

  /**
   * Send configuration data to the backend.
   * @param config - Configuration object containing system settings.
   * @returns Observable<any> - Response from the backend.
   */
  sendConfiguration(config: Record<string, any>): Observable<any> {
    return this.http.post(`${this.apiUrl}/configure`, config);
  }

  /**
   * Fetch logs from the backend.
   * @returns Observable<string[]> - Array of log entries.
   */
  getLogs(): Observable<string[]> {
    return this.http.get<string[]>(`${this.apiUrl}/logs`);
  }

  /**
   * Fetch the current ticket status from the backend.
   * @returns Observable<any> - Current status of the ticket pool.
   */
  getTicketStatus(): Observable<any> {
    return this.http.get<any>(`${this.apiUrl}/ticket-status`);
  }

  /**
   * Start the ticketing system.
   * @returns Observable<any> - Backend response indicating system start success.
   */
  startSystem(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/start`, {});
  }

  /**
   * Stop the ticketing system.
   * @returns Observable<any> - Backend response indicating system stop success.
   */
  stopSystem(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/stop`, {});
  }

  /**
   * Exit the ticketing system.
   * @returns Observable<any> - Backend response indicating system exit success.
   */
  exitSystem(): Observable<any> {
    return this.http.post<any>(`${this.apiUrl}/exit`, {});
  }
}