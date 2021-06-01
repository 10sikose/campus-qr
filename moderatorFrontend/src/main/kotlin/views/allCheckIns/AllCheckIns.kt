package views.allCheckIns

import com.studo.campusqr.common.ActiveCheckIn
import com.studo.campusqr.common.ReportData
import com.studo.campusqr.common.UserData
import com.studo.campusqr.common.emailSeparators
import com.studo.campusqr.common.extensions.emptyToNull
import com.studo.campusqr.common.extensions.format
import com.studo.campusqr.common.CheckInInfo
import kotlinext.js.js
import kotlinx.html.js.onSubmitFunction
import muiDatePicker
import org.w3c.dom.events.Event
import react.*
import react.dom.div
import react.dom.form
import util.Strings
import util.apiBase
import util.fileDownload
import util.get
import views.common.centeredProgress
import views.common.renderLinearProgress
import views.common.spacer
import webcore.*
import webcore.extensions.addDays
import webcore.extensions.inputValue
import webcore.extensions.launch
import webcore.materialUI.*
import kotlin.js.Date
import kotlin.js.json

interface AllCheckInsProps : RProps {
  var classes: AllCheckInsClasses
}

interface AllCheckInsState : RState {
  var snackbarText: String
  var checkIns: Array<CheckInInfo>
}

class AllCheckIns : RComponent<AllCheckInsProps, AllCheckInsState>() {
  override fun AllCheckInsState.init() {
    snackbarText = ""
  }

  private fun RBuilder.renderSnackbar() = mbSnackbar(
    MbSnackbarProps.Config(
      show = state.snackbarText.isNotEmpty(),
      message = state.snackbarText,
      onClose = {
        setState {
          snackbarText = ""
        }
      })
  )

  override fun componentDidMount() {
    fetchCheckIns {  }
  }

  private fun fetchCheckIns(block: () -> Unit) = launch {
    val fetchedUserData = NetworkManager.get<Array<CheckInInfo>>("$apiBase/allCheckIns/listAllCheckIns")
    if (fetchedUserData != null) {
      setState {
        checkIns = fetchedUserData
      }
    }
  }

  override fun RBuilder.render() {
    renderSnackbar()
    typography {
      attrs.variant = "h5"
      attrs.className = props.classes.content
      +Strings.all_check_ins.get()
    }

    if(state.checkIns?.isNotEmpty() == true) {
      mTable {
        mTableHead {
          mTableRow {
            mTableCell {
              typography {
                +"Email"
              }
            }
            mTableCell {
              typography {
                +"Check-In Date"
              }
            }
            mTableCell {
              typography {
                +"Seat"
              }
            }
          }
        }
        mTableBody {
          state.checkIns!!.forEach { checkIn ->
            mTableRow {
              mTableCell {
                typography {
                  +checkIn.email
                }
              }
              mTableCell {
                typography {
                  +checkIn.checkInDate
                }
              }
              mTableCell {
                typography {
                  +checkIn.seat.toString()
                }
              }
            }
          }
        }
      }
    }

  }
}

interface AllCheckInsClasses {
  var content: String
}

private val style = { _: dynamic ->
  js {
    content = js {
      margin = 16
    }
  }
}

private val styled = withStyles<AllCheckInsProps, AllCheckIns>(style)

fun RBuilder.renderAllCheckIns() = styled {
  // Set component attrs here
}