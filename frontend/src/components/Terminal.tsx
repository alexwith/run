import ansi from "../common/ansi";
import { TerminalIcon } from "../common/icons";
import { ProgramStatus } from "../common/types";
import { useStore } from "../store/store";

export default function Terminal() {
  const terminal = useStore((state) => state.terminal);
  const programStatus = useStore((state) => state.programStatus);

  return (
    <div className="border-[1px] border-light-gray rounded-lg overflow-hidden bg-gray text-white text-sm p-2 font-[Tahoma]">
      <div className="flex justify-between border-b-2 border-light-gray">
        <div className="flex items-center space-x-1">
          <h1 className="font-bold text-">Terminal</h1>
          <TerminalIcon size={20} />
        </div>
      </div>
      <div className="flex flex-col gap-[0.5px] mt-2 whitespace-pre-wrap font-mono text-s font-semibold">
        {terminal.map((line, i) => (
          <p
            key={i}
            className="text-white"
            dangerouslySetInnerHTML={{ __html: ansi.toHtml(line) }}
          ></p>
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
