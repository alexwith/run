import { RunIcon, SettingsIcon } from "../common/icons";
import { ProgramStatus } from "../common/types";
import { useStore } from "../store/store";
import Button from "./common/Button";
import LanguageSelector from "./LanguageSelector";

export default function MenuBar() {
  const language = useStore((state) => state.language);
  const code = useStore((state) => state.code);

  const setProgramStatus = useStore((actions) => actions.setProgramStatus);
  const addToTerminal = useStore((actions) => actions.addToTerminal);
  const clearTerminal = useStore((actions) => actions.clearTerminal);

  const handleRunClick = () => {
    clearTerminal();
    setProgramStatus(null);

    const socket = new WebSocket(
      `ws://localhost:8080/socket/v1/execute?language=${language.image}`,
    );
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
    <div className="flex justify-between">
      <Button name="Run" icon={<RunIcon size={14} />} onClick={handleRunClick} />
      <div className="flex gap-2">
        <LanguageSelector />
        <Button icon={<SettingsIcon size={20} />} />
      </div>
    </div>
  );
}
