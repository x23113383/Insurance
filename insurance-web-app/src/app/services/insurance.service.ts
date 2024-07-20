import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Driver } from '../models/driver';
import { Quote } from '../models/quote';

@Injectable()
export class InsuranceService {
  baseUrl: string = 'https://insurancerestservice-1690701015078.azurewebsites.net';
  // baseUrl: string = 'http://localhost:8080';
  calculateParam: string = '/calculate';
  quotesParam: string = '/quotes';

  constructor(private http: HttpClient) {}

  /**
   * This method generates a new insurance quote for the provided driver information object and returns the quote's reference id.
   * @param driver contains information about the driver
   * @returns observable of new quote's reference id
   */
  public getNewQuote(driver: Driver) {
    return this.http.post(this.baseUrl + this.calculateParam, driver, { responseType: 'text' });
  }

  /**
   * This method retrieved the insurance quote for the provided reference id.
   * @param reference a 16 character alphanumeric string
   * @returns observable of quote information object
   */
  public getQuoteByReference(reference: string) {
    return this.http.get<Quote>(this.baseUrl + this.quotesParam, {
      params: {
        reference: reference,
      },
    });
  }
}
