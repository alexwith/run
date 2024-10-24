import CodeEditor from "./components/CodeEditor";
import MenuBar from "./components/MenuBar";
import Terminal from "./components/Terminal";

function App() {
  return (
    <>
      <div className="w-screen h-screen flex justify-center">
        <div className="flex flex-col gap-2 mt-[30%]">
          <MenuBar />
          <CodeEditor />
          <Terminal />
        </div>
      </div>
    </>
  );
}

export default App;
