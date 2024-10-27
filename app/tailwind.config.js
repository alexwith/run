/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        dark: "#1b1b1f",
        gray: "#202127",
        "light-gray": "#2b2f36",
      },
    },
  },
  plugins: [],
};
