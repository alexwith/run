import CodeEditor from "./components/CodeEditor";
import MenuBar from "./components/MenuBar";
import Timeline from "./components/timeline/Timeline";
import Terminal from "./components/Terminal";

function App() {
  return (
    <>
      <div className="w-screen h-screen flex items-center flex-col">
        <div className="m-10">
          <div className="flex items-center justify-center">
            <span className="absolute mx-auto py-4 flex items-center border w-fit bg-sky-400 blur-xl bg-clip-text text-6xl box-content font-extrabold text-transparent text-center select-none">
              <img className="w-16" src="/logo.svg" />
              <span>Run</span>
            </span>
            <h1 className="relative top-0 w-fit h-auto py-4 justify-center flex bg-sky-400 items-center bg-clip-text text-6xl font-extrabold text-transparent text-center select-auto">
              <img className="w-16" src="/logo.svg" />
              <span>Run</span>
            </h1>
          </div>
          <p className="text-zinc-500 font-semibold text-xl text-center">
            Execute code quickly in the browser!
          </p>
        </div>
        <div className="flex flex-col gap-2 max-w-[650px] w-full px-5">
          <MenuBar />
          <CodeEditor />
          <Timeline />
          <Terminal />
        </div>
      </div>
    </>
  );
}

export default App;
