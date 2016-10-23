import {NoteDto} from "./note-dto";

/**
 * Represents a DTO for a note with ID.
 */
export interface NoteWithIdDto extends NoteDto {
    id: number;
}
