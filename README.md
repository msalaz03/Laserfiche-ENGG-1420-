<h1>Project Overview</h1>
<p>The objective of this project is to create a Java-based file processing system for Laserfiche, a California-based company. The system will exhibit robustness by efficiently handling diverse scenarios, including sorting, filtering, and transforming files based on user-defined conditions.</p>

<h2>Entries</h2>
<p>Entries
Each entry refers to a file or directory. There are two types of entries:
Local Entry: which is represented by a string value and refers to a file or a directory on the local
file system. Some examples: A local entry with the value ‘c:\sample\txt\addresses.txt’, refers to a single file on the
local file system. A local entry with the value ‘c:\sample\txt’, refers to a single directory on the local file
system. Remote Entry: which refers to a file or a directory on a Laserfiche Repository on the cloud. Each
remote entry is determined by a pair of values (repoId, entryId) where repoId is the id of the Laserfiche Repository where the entry resides. This is a string with
a maximum length of 20 characters, e.g. “r-34w6” entryId is the id of the entry. This is an integer greater than 0. Note that simply by
looking at the value of an entryId, we cannot say whether it belongs to a file or a
directory.</p>




<h2>Processing Elements</h2>
<p>Processing Elements
Each processing element is responsible for a unit of processing in the scenario. Each processing element
takes a list of entries as input, performs some operation on them, and then generates a list of entries as
output. A scenario includes a sequence of processing elements, so that the output of a processing
element is the input of its successor processing element.</p>

<br>
<table>
  <tr>
    <th>Element</th>
    <th>Description</th>
    <th>Input</th>
    <th>Output</th>
  </tr>


  <tr>
    <td>Name Filter</td>
    <td>Filters entries based on the presence of a given string in their name.</td>
    <td>List of entries, String Key</td>
    <td>Sub-list of entries with the given string Key in their name</td>
  </tr>


  <tr>
    <td>Length Filter</td>
    <td>Filters files based on their length and a specified condition.</td>
    <td>List of entries, Long Length, String Operator</td>
    <td>Sub-list of entries satisfying the given length condition</td>
  </tr>


  <tr>
    <td>Content Filter</td>
    <td>Filters files based on the presence of a given string in their content.</td>
    <td>List of entries, String Key</td>
    <td>Sub-list of entries with the given string Key in their content</td>
  </tr>


  <tr>
    <td>Count Filter</td>
    <td>Filters files based on the count of a given string in their content.</td>
    <td>List of entries, String Key, Integer Min</td>
    <td>Sub-list of entries with at least Min occurrences of the given string Key</td>
  </tr>


  <tr>
    <td>Split Processing Element</td>
    <td>Splits files into smaller parts based on the specified number of lines.</td>
    <td>List of entries, Integer Lines</td>
    <td>List of created entries resulted from splitting the input</td>
  </tr>


  <tr>
    <td>List Processing Element</td>
    <td>Selects a list of entries from directories based on a specified maximum count.</td>
    <td>List of entries, Integer Max</td>
    <td>List of entries selected from the directories</td>
  </tr>

  <tr>
    <td>Rename Processing Element</td>
    <td>Appends a given suffix to the name of each entry in the input list.</td>
    <td>List of entries, String Suffix</td>
    <td>List of entries with updated names</td>
  </tr>


  <tr>
    <td>Print Processing Element</td>
    <td>Prints information about each entry, including name, length, and absolute path.</td>
    <td>List of entries</td>
    <td>No change to the list of entries</td>
  </tr>
</table>
