import { RunIcon } from "./common/icons";
import CodeEditor from "./components/CodeEditor";
import Terminal from "./components/Terminal";

function App() {
  return (
    <>
      <div className="w-screen h-screen flex justify-center">
        <div className="flex flex-col gap-2 mt-[30%]">
          <div className="flex items-center gap-1 bg-sky-500 text-white font-bold w-fit px-2 rounded-md hover:cursor-pointer">
            <span>Run</span> <RunIcon size={14} />
          </div>
          <CodeEditor />
          <Terminal />
        </div>
      </div>
    </>
  );
}

export default App;
