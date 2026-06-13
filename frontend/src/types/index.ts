export interface WordCategory { id: number; name: string; description: string; icon: string }
export interface Word { id: number; word: string; translation: string; pronunciation: string; partOfSpeech: string; example: string; category: WordCategory; difficulty: string }
export interface RegexExample { id: number; name: string; pattern: string; description: string; exampleText: string }
