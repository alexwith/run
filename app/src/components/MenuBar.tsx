import { RunIcon } from "../common/icons";
import { ProgramStatus } from "../common/types";
import { useStore } from "../store/store";

export default function MenuBar() {
  const code = useStore((state) => state.code);

  const setProgramStatus = useStore((actions) => actions.setProgramStatus);
  const addToTerminal = useStore((actions) => actions.addToTerminal);
  const clearTerminal = useStore((actions) => actions.clearTerminal);

  const handleRunClick = () => {
    clearTerminal();
    setProgramStatus(null);

    const socket = new WebSocket(`ws://localhost:8080/socket/v1/execute?language=${"kotlin"}`);
    socket.onmessage = (event) => {
      const { data } = event;
      switch (data) {
        case "run:ready": {
          socket.send(code);
          break;
        }
        case "run:building": {
          setProgramStatus(ProgramStatus.Building);
          break;
        }
        case "run:running": {
          setProgramStatus(ProgramStatus.Running);
          break;
        }
        default: {
          addToTerminal(event.data);
          break;
        }
      }
    };

    socket.onclose = () => {
      setProgramStatus(ProgramStatus.Executed);
    };
  };

  return (
    <div
      className="flex items-center gap-1 bg-sky-500 text-white font-bold w-fit px-2 rounded-md hover:cursor-pointer"
      onClick={handleRunClick}
    >
      <span>Run</span> <RunIcon size={14} />
    </div>
  );
}
