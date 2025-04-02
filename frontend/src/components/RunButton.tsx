import { RunIcon } from "../common/icons";
import Button from "./common/Button";
import { useState } from "react";
import { ProgramStatus } from "../common/types";
import { useStore } from "../store/store";
import { QUEUE_TIMEOUT } from "../common/constants";

export default function RunButton() {
  const [socket, setSocket] = useState<WebSocket | null>(null);

  const language = useStore((state) => state.language);
  const code = useStore((state) => state.code);

  const setProgramStatus = useStore((actions) => actions.setProgramStatus);
  const addToTerminal = useStore((actions) => actions.addToTerminal);
  const clearTerminal = useStore((actions) => actions.clearTerminal);

  const handleRunClick = () => {
    if (socket) {
      return;
    }

    clearTerminal();
    setProgramStatus(ProgramStatus.Queueing);

    const localSocket = new WebSocket(
      `wss://run.alexwith.com:8080/socket/v1/execute?language=${language.image}`,
    );
    setSocket(localSocket);

    const timeout = setTimeout(() => {
      setSocket(null);
      setProgramStatus(ProgramStatus.Failed);
    }, QUEUE_TIMEOUT);

    localSocket.onmessage = (event) => {
      const { data } = event;
      switch (data) {
        case "run:ready": {
          localSocket.send(code);
          break;
        }
        case "run:building": {
          clearTimeout(timeout);
          setProgramStatus(ProgramStatus.Building);
          break;
        }
        case "run:running": {
          setProgramStatus(ProgramStatus.Running);
          break;
        }
        case "run:failed": {
          setProgramStatus(ProgramStatus.Failed);
          break;
        }
        case "run:executed": {
          setProgramStatus(ProgramStatus.Executed);
          break;
        }
        default: {
          addToTerminal(event.data);
          break;
        }
      }
      localSocket.onclose = () => {
        setSocket(null);
      };
    };
  };

  if (socket) {
    return (
      <Button
        name="Running"
        icon={
          <svg
            className="animate-spin"
            xmlns="http://www.w3.org/2000/svg"
            width="1.2em"
            height="1.2em"
            viewBox="0 0 24 24"
          >
            <path
              fill="currentColor"
              d="M10.14,1.16a11,11,0,0,0-9,8.92A1.59,1.59,0,0,0,2.46,12,1.52,1.52,0,0,0,4.11,10.7a8,8,0,0,1,6.66-6.61A1.42,1.42,0,0,0,12,2.69h0A1.57,1.57,0,0,0,10.14,1.16Z"
            />
          </svg>
        }
      />
    );
  }

  return <Button name="Run" icon={<RunIcon size={14} />} onClick={handleRunClick} />;
}
