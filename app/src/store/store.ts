import { create } from "zustand";
import { ProgramStatus } from "../common/types";
import { Language, languages } from "../common/languages";

interface State {
  language: Language;
  code: string;
  programStatus: ProgramStatus | null;
  terminal: string[];
}

interface Actions {
  setLanguage: (language: Language) => void;
  setCode: (code: string) => void;
  setProgramStatus: (status: ProgramStatus | null) => void;
  addToTerminal: (line: string) => void;
  clearTerminal: () => void;
}

export const useStore = create<State & Actions>((set) => ({
  language: languages.get("python")!,
  code: languages.get("python")!.example,
  programStatus: null,
  terminal: [],

  setLanguage: (language: Language) => set(() => ({ language })),
  setCode: (code: string) => set(() => ({ code })),
  setProgramStatus: (status: ProgramStatus | null) => set(() => ({ programStatus: status })),
  addToTerminal: (line: string) => set((state) => ({ terminal: [...state.terminal, line] })),
  clearTerminal: () => set(() => ({ terminal: [] })),
}));
