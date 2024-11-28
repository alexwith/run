import AnsiConverter from "ansi-to-html";

const ansi = new AnsiConverter({
  colors: {
    0: "#555555",
    1: "#ff5555",
    2: "#55ff55",
    3: "#ffff55",
    4: "#5555ff",
    5: "#ff55ff",
    6: "#55ffff",
    7: "#ffffff",
  },
});

export default ansi;
