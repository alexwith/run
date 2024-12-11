/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{ts,tsx}"],
  theme: {
    extend: {
      colors: {
        dark: "#0c1014",
        gray: "#1a1e25",
        "light-gray": "#2c3140",
      },
      animation: {
        "processing-bar": "processing_bar 5s linear infinite",
      },
      keyframes: {
        processing_bar: {
          "0%": { backgroundPosition: "500% 0%" },
          "100%": { backgroundPosition: "0% 0%" },
        },
      },
    },
  },
  plugins: [],
};
