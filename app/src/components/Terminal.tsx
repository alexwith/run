import { ChevronRightIcon } from "../common/icons";
import { ProgramStatus } from "../common/types";
import { useStore } from "../store/store";

export default function Terminal() {
  const terminal = useStore((state) => state.terminal);
  const programStatus = useStore((state) => state.programStatus);

  return (
    <div className="border-[1px] border-light-gray rounded-lg overflow-hidden bg-gray text-white text-sm p-2 font-[Tahoma]">
      <div className="flex justify-between">
        <h1 className="font-bold text-indigo-200">Terminal</h1>
        {programStatus && <p className="text-xs text-sky-400 font-bold">{programStatus}</p>}
      </div>
      <div className="flex flex-col gap-1">
        {terminal.map((line, i) => (
          <p key={i} className="flex items-center gap-1">
            <ChevronRightIcon className="text-zinc-500" />
            <span className="text-indigo-200">{line}</span>
          </p>
        ))}
        {programStatus === ProgramStatus.Running && (
          <div className="flex gap-1 mt-1">
            <div className="w-2 h-2 bg-zinc-500 rounded-full animate-bounce"></div>
            <div className="w-2 h-2 bg-zinc-500 rounded-full animate-bounce [animation-delay:-0.3s]"></div>
            <div className="w-2 h-2 bg-zinc-500 rounded-full animate-bounce [animation-delay:-0.15s]"></div>
          </div>
        )}
      </div>
    </div>
  );
}
