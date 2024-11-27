import { pythonLanguage } from "@codemirror/lang-python";
import { tags } from "@lezer/highlight";
import CodeMirror from "@uiw/react-codemirror";
import { useStore } from "../store/store";
import createTheme from "@uiw/codemirror-themes";

const myTheme = createTheme({
  theme: "light",
  settings: {
    background: "#202127",
    foreground: "#787c99",
    caret: "#c0caf5",
    selection: "#515c7e40",
    selectionMatch: "#16161e",
    gutterBackground: "#202127",
    gutterForeground: "#787c99",
    gutterBorder: "transparent",
    lineHighlight: "#474b6611",
  },
  styles: [
    { tag: tags.keyword, color: "#bb9af7" },
    { tag: [tags.name, tags.deleted, tags.character, tags.macroName], color: "#c0caf5" },
    { tag: [tags.propertyName], color: "#7aa2f7" },
    {
      tag: [tags.processingInstruction, tags.string, tags.inserted, tags.special(tags.string)],
      color: "#9ece6a",
    },
    { tag: [tags.function(tags.variableName), tags.labelName], color: "#7aa2f7" },
    { tag: [tags.color, tags.constant(tags.name), tags.standard(tags.name)], color: "#bb9af7" },
    { tag: [tags.definition(tags.name), tags.separator], color: "#c0caf5" },
    { tag: [tags.className], color: "#c0caf5" },
    {
      tag: [tags.number, tags.changed, tags.annotation, tags.modifier, tags.self, tags.namespace],
      color: "#ff9e64",
    },
    { tag: [tags.typeName], color: "#0db9d7" },
    { tag: [tags.operator, tags.operatorKeyword], color: "#bb9af7" },
    { tag: [tags.url, tags.escape, tags.regexp, tags.link], color: "#b4f9f8" },
    { tag: [tags.meta, tags.comment], color: "#444b6a" },
    { tag: tags.strong, fontWeight: "bold" },
    { tag: tags.emphasis, fontStyle: "italic" },
    { tag: tags.link, textDecoration: "underline" },
    { tag: tags.heading, fontWeight: "bold", color: "#89ddff" },
    { tag: [tags.atom, tags.bool, tags.special(tags.variableName)], color: "#c0caf5" },
    { tag: tags.invalid, color: "#ff5370" },
    { tag: tags.strikethrough, textDecoration: "line-through" },
  ],
});

export default function CodeEditor() {
  const code = useStore((state) => state.code);

  const setCode = useStore((actions) => actions.setCode);

  return (
    <div className="border-[1px] border-light-gray rounded-lg overflow-hidden shadow-[0px_0px_35px_5px_rgba(56,_189,_248,_0.05)]">
      <CodeMirror
        height="350px"
        width="650px"
        value={code}
        extensions={[pythonLanguage]}
        theme={myTheme}
        onChange={setCode}
      />
    </div>
  );
}
