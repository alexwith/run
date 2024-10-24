import { RunIcon } from "../common/icons";
import { useStore } from "../store/store";

export default function MenuBar() {
  const code = useStore((state) => state.code);

  const addToTerminal = useStore((actions) => actions.addToTerminal);
  const clearTerminal = useStore((actions) => actions.clearTerminal);

  const handleRunClick = () => {
    clearTerminal();

    fetch("http://localhost:8080/api/v1/execute", {
      method: "POST",
      body: JSON.stringify({
        language: "Python",
        code,
      }),
      headers: {
        "Content-type": "application/json; charset=UTF-8",
      },
    }).then((response) => {
      if (response.status === 200) {
        console.log("All good");

        const socket = new WebSocket("ws://localhost:8080/test");
        socket.onmessage = (event) => {
          addToTerminal(event.data);
        };

        socket.onclose = () => {
          console.log("socket closed");
        };
        return;
      }

      console.log("Failed to run code");
    });
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
