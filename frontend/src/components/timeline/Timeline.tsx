import { ProgramStatus, TimelineState } from "../../common/types";
import { useStore } from "../../store/store";
import TimelineBar from "./TimelineBar";
import TimelinePoint from "./TimelinePoint";

function getTimelineState(relative: ProgramStatus, current: ProgramStatus | null): TimelineState {
  if (relative === current) {
    return TimelineState.Active;
  }

  if (!current || relative > current) {
    return TimelineState.Pending;
  }

  return TimelineState.Complete;
}

export default function ProgressBar() {
  const programStatus = useStore((state) => state.programStatus);

  return (
    <div className="flex justify-between items-center mb-6 mt-2 gap-1 w-[90%] ml-auto mr-auto">
      <TimelinePoint
        name={"Build"}
        state={getTimelineState(ProgramStatus.Building, programStatus)}
      />
      <TimelineBar state={getTimelineState(ProgramStatus.Building, programStatus)} />
      <TimelinePoint name={"Run"} state={getTimelineState(ProgramStatus.Running, programStatus)} />
      <TimelineBar state={getTimelineState(ProgramStatus.Running, programStatus)} />
      <TimelinePoint
        name={"Finish"}
        state={getTimelineState(ProgramStatus.Executed, programStatus)}
      />
    </div>
  );
}
