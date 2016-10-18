import {NgModule, Injectable, OnInit} from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Component } from "@angular/core";
import {Http, Response, HttpModule} from "@angular/http";
import {FormsModule} from "@angular/forms";

interface NoteDto {
    text: string;
}

interface NoteWithIdDto extends NoteDto {
    id: number;
}

@Injectable()
export class ApiClient {
    constructor(private http: Http) {
    }

    async getNotes(): Promise<NoteWithIdDto[]> {
        const response: Response = await this.http.get('http://localhost:9090/api/notes').toPromise();
        const body: NoteWithIdDto[] = response.json();
        return body;
    }

    async getNote(noteId: number): Promise<NoteWithIdDto> {
        const response: Response = await this.http.get(`http://localhost:9090/api/notes/${noteId}`).toPromise();
        const body: NoteWithIdDto = response.json();
        return body;
    }

    async createNote(noteDto: NoteDto): Promise<NoteWithIdDto> {
        const createNoteResponse: Response = await this.http.post('http://localhost:9090/api/notes', noteDto).toPromise();
        const createdNoteLocation: string = createNoteResponse.headers.get('location');
        
        const getCreatedNoteResponse = await this.http.get(createdNoteLocation).toPromise();
        const createdNoteDto: NoteWithIdDto = getCreatedNoteResponse.json();
        
        return createdNoteDto;
    }

    async updateNote(noteId: number, noteDto: NoteDto): Promise<NoteWithIdDto> {
        const noteUrl: string = `http://localhost:9090/api/notes/${noteId}`;
        const updateNoteResponse: Response = await this.http.put(noteUrl, noteDto).toPromise();

        const getUpdatedNoteResponse = await this.http.get(noteUrl).toPromise();
        const updatedNoteDto: NoteWithIdDto = getUpdatedNoteResponse.json();

        return updatedNoteDto;
    }

    async deleteNote(noteId: number): Promise<void> {
        const noteUrl = `http://localhost:9090/api/notes/${noteId}`;
        const response: Response = await this.http.delete(noteUrl).toPromise();
    }
}

interface Note {
    id: number;
    text: string;
}

@Component({
    selector: 'app',
    template: `
<div>
    <form (ngSubmit)="createNote()">
        <input type="text" [(ngModel)]="newNoteText" name="text">
        <button type="submit">Create</button>
    </form>
    <div *ngIf="notes.length > 0">
        <p>There are {{notes.length}} notes</p>
        <ul>
            <li *ngFor="let note of notes;trackBy:id">
                <span>id={{note.id}}</span>
                <span>text={{note.text}}</span>
                <button type="button" (click)="updateNote(note.id)">Update</button>
                <button type="button" (click)="deleteNote(note.id)">Delete</button>
            </li>
        </ul>
    </div>
    <div *ngIf="notes.length == 0">There are no notes</div> 
</div>
`
})
class AppComponent implements OnInit {
    public notes: Note[] = [];
    public newNoteText: string = '';

    constructor(private apiClient: ApiClient) {
    }

    async ngOnInit(): Promise<void> {
        const noteDtos: NoteWithIdDto[] = await this.apiClient.getNotes();
        this.notes = noteDtos.map((noteDto) => {
            return this.noteFromNoteWithIdDto(noteDto);
        })
    }

    async createNote(): Promise<void> {
        const text: string = this.newNoteText;
        if(text == '') {
            return;
        }

        this.newNoteText = '';

        const noteDto: NoteDto = {
            text: text
        };

        const createdNoteDto: NoteWithIdDto = await this.apiClient.createNote(noteDto);
        const note: Note = this.noteFromNoteWithIdDto(createdNoteDto);
        this.notes.push(note);
    }

    async updateNote(noteId: number): Promise<void> {
        const updatedNoteDto: NoteDto = {
            text: 'Updated note'
        };
        const actualUpdatedNoteDto: NoteWithIdDto = await this.apiClient.updateNote(noteId, updatedNoteDto);
        this.notes = this.notes.map(note => {
            if(note.id != actualUpdatedNoteDto.id) {
                return note;
            }

            return this.noteFromNoteWithIdDto(actualUpdatedNoteDto);
        });
    }

    async deleteNote(noteId: number): Promise<void> {
        await this.apiClient.deleteNote(noteId);
        this.notes = this.notes.filter(note => {
            if(note.id != noteId) {
                return true;
            }

            return false;
        });
    }

    private noteFromNoteWithIdDto(noteWithIdDto: NoteWithIdDto): Note {
        return {
            id: noteWithIdDto.id,
            text: noteWithIdDto.text
        };
    }
}

@NgModule({
    imports: [ BrowserModule, FormsModule, HttpModule ],
    declarations: [
        AppComponent
    ],
    providers: [ ApiClient ],
    bootstrap: [ AppComponent ]
})
export class AppModule {
}
