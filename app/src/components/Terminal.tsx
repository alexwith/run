import { ChevronRightIcon } from "../common/icons";
import { useStore } from "../store/store";

export default function Terminal() {
  const terminal = useStore((state) => state.terminal);

  return (
    <div className="border-[1px] border-zinc-800 rounded-lg overflow-hidden bg-[#1A1B26] text-white text-sm p-2 font-[Tahoma]">
      <h1 className="font-bold text-indigo-200">Terminal</h1>
      <div className="flex flex-col gap-1">
        {terminal.map((line, i) => (
          <p key={i} className="flex items-center gap-1">
            <ChevronRightIcon className="text-zinc-500" />
            <span className="text-indigo-200">{line}</span>
          </p>
        ))}
      </div>
    </div>
  );
}
