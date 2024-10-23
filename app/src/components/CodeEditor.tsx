import { pythonLanguage } from "@codemirror/lang-python";
import { tokyoNight } from "@uiw/codemirror-theme-tokyo-night";
import CodeMirror from "@uiw/react-codemirror";

export function CodeEditor() {
  return (
    <div className="border-[1px] border-zinc-800 rounded-lg overflow-hidden shadow-[0px_0px_20px_0px_rgba(14,_165,_233,_0.05)]">
      <CodeMirror height="300px" width="500px" extensions={[pythonLanguage]} theme={tokyoNight} />
    </div>
  );
}
