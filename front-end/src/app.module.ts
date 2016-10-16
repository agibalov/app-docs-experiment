import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Component } from "@angular/core";

@Component({
    selector: 'app',
    template: `<div>
    <h1>counter is {{count}}</h1>
    <button type="button" (click)="increment()">Increment</button>    
  </div>`
})
class AppComponent {
    public count: number = 0;

    increment(): void {
        ++this.count;
    }
}

@NgModule({
    imports: [ BrowserModule ],
    declarations: [
        AppComponent
    ],
    providers: [ ],
    bootstrap: [ AppComponent ]
})
export class AppModule {
}
