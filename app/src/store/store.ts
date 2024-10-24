import { create } from "zustand";

interface State {
  code: string;
  terminal: string[];
}

interface Actions {
  setCode: (code: string) => void;
  addToTerminal: (line: string) => void;
  clearTerminal: () => void;
}

export const useStore = create<State & Actions>((set) => ({
  code: 'print("Hello World")',
  terminal: [],

  setCode: (code: string) => set(() => ({ code })),
  addToTerminal: (line: string) => set((state) => ({ terminal: [...state.terminal, line] })),
  clearTerminal: () => set(() => ({ terminal: [] })),
}));
