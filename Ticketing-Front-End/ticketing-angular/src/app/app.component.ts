import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { TicketStatusComponent } from './page/ticket-status/ticket-status.component';
import { ControlPanelComponent } from './page/control-panel/control-panel.component';
import { SystemLogsComponent } from './page/system-logs/system-logs.component';
import { ConfigurationSettingsComponent } from './page/configuration-settings/configuration-settings.component';

@Component({
  selector: 'app-root',
  standalone: true,
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    TicketStatusComponent,
    ControlPanelComponent,
    SystemLogsComponent, // Correctly imported
    ConfigurationSettingsComponent,
  ],
})
export class AppComponent {
  title = 'Real-Time Ticketing System';
}
