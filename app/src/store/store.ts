import { create } from "zustand";

interface State {
  terminal: string[];
}

interface Actions {
  addToTerminal: (line: string) => void;
}

export const useStore = create<State & Actions>((set) => ({
  terminal: [],

  addToTerminal: (line: string) => set((state) => ({ terminal: [line, ...state.terminal] })),
}));
