import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpModule } from "@angular/http";
import { FormsModule } from "@angular/forms";
import {ApiClient} from "./api-client";
import {AppComponent} from "./app.component";
import {TransactionRecorder} from "./transaction-recorder";
import {HttpWrapper} from "./http-wrapper";

/**
 * Represent the main application module.
 */
@NgModule({
    imports: [ BrowserModule, FormsModule, HttpModule ],
    declarations: [
        AppComponent
    ],
    providers: [ ApiClient, TransactionRecorder, HttpWrapper ],
    bootstrap: [ AppComponent ]
})
export class AppModule {
}
