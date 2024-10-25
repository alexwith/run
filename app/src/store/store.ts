import { create } from "zustand";
import { ProgramStatus } from "../common/types";
import { codeExamples } from "../common/codeExamples";

interface State {
  code: string;
  programStatus: ProgramStatus | null;
  terminal: string[];
}

interface Actions {
  setCode: (code: string) => void;
  setProgramStatus: (status: ProgramStatus | null) => void;
  addToTerminal: (line: string) => void;
  clearTerminal: () => void;
}

export const useStore = create<State & Actions>((set) => ({
  code: codeExamples.get("kotlin")!,
  programStatus: null,
  terminal: [],

  setCode: (code: string) => set(() => ({ code })),
  setProgramStatus: (status: ProgramStatus | null) => set(() => ({ programStatus: status })),
  addToTerminal: (line: string) => set((state) => ({ terminal: [...state.terminal, line] })),
  clearTerminal: () => set(() => ({ terminal: [] })),
}));
