import {Note} from "./note";
import {NoteWithIdDto} from "./note-with-id-dto";
import {NoteDto} from "./note-dto";
import {Component, OnInit} from "@angular/core";
import {ApiClient} from "./api-client";

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
export class AppComponent implements OnInit {
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
