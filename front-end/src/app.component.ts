import {Note} from "./note";
import {NoteWithIdDto} from "./note-with-id-dto";
import {NoteDto} from "./note-dto";
import {Component, OnInit} from "@angular/core";
import {ApiClient} from "./api-client";
import {Wove} from "aspect.js-angular";

/**
 * A component that represents the one and only application page.
 */
@Wove()
@Component({
    selector: 'app',
    template: `
<nav class="navbar navbar-default">
  <div class="container">
    <div class="navbar-header">
      <a class="navbar-brand" href="#">Notepad</a>
    </div>
</div>
</nav>
<div class="container">
    <form (ngSubmit)="createNote()" class="form-inline">
        <div class="form-group">
            <input type="text" [(ngModel)]="newNoteText" placeholder="Type note text here" name="text" class="form-control input-lg">
        </div>
        <button type="submit" class="btn btn-default btn-lg">Create</button>
    </form>
    <div *ngIf="notes.length > 0">
        <table class="table">
            <caption>There are {{notes.length}} notes</caption>
            <tr *ngFor="let note of notes;trackBy:id">
                <td class="col-md-1"><span class="badge">{{note.id}}</span></td>
                <td class="col-md-8">{{note.text}}</td>
                <td class="col-md-3 text-right">
                    <button type="button" (click)="updateNote(note.id)" class="btn btn-default">Update</button>
                    <button type="button" (click)="deleteNote(note.id)" class="btn btn-danger">Delete</button>
                </td>
            </tr>
        </table>
    </div>
    <div *ngIf="notes.length == 0" class="alert alert-info">There are no notes</div> 
</div>
`
})
export class AppComponent implements OnInit {
    public notes: Note[] = [];
    public newNoteText: string = '';

    constructor(private apiClient: ApiClient) {
    }

    /**
     * Initialize - retrieve a collection of all notes.
     */
    async ngOnInit(): Promise<void> {
        const noteDtos: NoteWithIdDto[] = await this.apiClient.getNotes();
        this.notes = noteDtos.map((noteDto) => {
            return this._noteFromNoteWithIdDto(noteDto);
        })
    }

    /**
     * Create a note based on user input.
     */
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
        const note: Note = this._noteFromNoteWithIdDto(createdNoteDto);
        this.notes.push(note);
    }

    /**
     * Update a note based on user input.
     * @param noteId an ID of note to update
     */
    async updateNote(noteId: number): Promise<void> {
        const updatedNoteDto: NoteDto = {
            text: 'Updated note'
        };
        const actualUpdatedNoteDto: NoteWithIdDto = await this.apiClient.updateNote(noteId, updatedNoteDto);
        this.notes = this.notes.map(note => {
            if(note.id != actualUpdatedNoteDto.id) {
                return note;
            }

            return this._noteFromNoteWithIdDto(actualUpdatedNoteDto);
        });
    }

    /**
     * Delete a note based on user input.
     * @param noteId an ID of note to delete.
     */
    async deleteNote(noteId: number): Promise<void> {
        await this.apiClient.deleteNote(noteId);
        this.notes = this.notes.filter(note => {
            if(note.id != noteId) {
                return true;
            }

            return false;
        });
    }

    /**
     * Construct a note from note DTO.
     * @param noteWithIdDto a note data transfer object
     * @returns {{id: number, text: string}} a constructed note
     */
    private _noteFromNoteWithIdDto(noteWithIdDto: NoteWithIdDto): Note {
        return {
            id: noteWithIdDto.id,
            text: noteWithIdDto.text
        };
    }
}
