import { ApplicationConfig, provideZoneChangeDetection } from '@angular/core';

export const appConfig: ApplicationConfig = {
  providers: [
    // Optimize zone change detection by coalescing multiple events into a single change detection cycle
    provideZoneChangeDetection({ eventCoalescing: true })
  ]
};