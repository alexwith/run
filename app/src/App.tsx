import { RunIcon } from "./common/icons";
import { CodeEditor } from "./components/CodeEditor";

function App() {
  return (
    <>
      <div className="w-screen h-screen flex justify-center items-center">
        <div>
          <div className="my-2">
            <div className="flex items-center gap-1 bg-sky-500 text-white font-bold w-fit px-2 rounded-md hover:cursor-pointer">
              <span>Run</span> <RunIcon size={14} />
            </div>
          </div>
          <CodeEditor />
        </div>
      </div>
    </>
  );
}

export default App;
